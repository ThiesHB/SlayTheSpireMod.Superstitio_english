package superstitioapi.utils.pathBuilder

import superstitioapi.utils.pathBuilder.RelativeScope.Companion.EMPTY_Relative_File_Scope
import kotlin.io.path.Path

// --- 1. 静态的 PathCombine 工具函数 ---
internal object PathUtil
{
    /**
     * 类似于 C# 的 Path.Combine。
     * 使用 java.nio.file.Path 在内部进行路径组合，确保跨平台和分隔符的正确性。
     * 返回组合后的路径字符串。
     *
     * @param basePath 基础路径字符串。
     * @param parts 要追加的路径部分。
     * @return 组合并规范化后的路径字符串。
     */
    @Throws(IllegalArgumentException::class)
    fun combine(basePath: String, vararg parts: String): String
    {
        if (basePath.isEmpty() && parts.all { it.isEmpty() })
        {
            return "" // 如果所有都为空，返回空
        }
        var currentPath = Path(basePath) // 允许 basePath 为空，Path("") 是有效的
        for (part in parts)
        {
            if (part.isNotEmpty())
            { // 只有非空部分才参与 resolve
                currentPath = currentPath.resolve(part)
            }
        }
        return currentPath.normalize().toString()
    }
}

//// --- 1. 共同的路径接口 IModPath ---
//interface IModPath
//{
//    /**
//     * 在当前路径下创建一个新的子作用域。
//     * 返回一个新的 IModPath 实例，其类型（相对或锚定）与当前实例相同。
//     * @param subPathParts 描述子作用域的路径片段。
//     */
//    fun createScope(vararg subPathParts: String): IModPath
//}

/**
 * 代表一个相对路径片段/作用域。
 * 使用 @JvmInline value class 来优化。
 */
@JvmInline
value class RelativeScope private constructor(
    // 内部存储，不对外暴露为公共API
    private val internalPathString: String
)
{

    val isEmpty: Boolean get() = internalPathString.isEmpty()

    fun createScope(vararg subPathParts: String): RelativeScope
    {
        if (subPathParts.all { it.isEmpty() }) return this
        return RelativeScope(PathUtil.combine(this.internalPathString, *subPathParts))
    }

    /**
     * RelativeModPath 能与另一个 RelativeModPath 组合成新的 RelativeModPath。
     */
    fun createScope(other: RelativeScope): RelativeScope
    {
        if (other.isEmpty) return this
        return RelativeScope(PathUtil.combine(this.internalPathString, other.internalPathString))
    }

    /**
     * 解读文件，但是得到的文件依旧是相对路径的。
     */
    fun resolveFile(fileName: String): RelativeFilePath
    {
        return RelativeFilePath(this.internalPathString, fileName)
    }

    /**
     * 固定在某个绝对路径上
     */
    fun pinToAbsolute(absoluteScope: AbsoluteScope): AbsoluteScope
    {
        if (this.isEmpty) return absoluteScope
        return absoluteScope.createScope(internalPathString)
    }

    /**
     * 固定在某个绝对路径上
     */
    fun pinToAbsolute(absoluteScope: AbsoluteScopeWithFormat): AbsoluteScopeWithFormat
    {
        if (this.isEmpty) return absoluteScope
        return absoluteScope.createScope(internalPathString)
    }

    /**
     * toString 仅用于调试，因为 value class 的 toString 默认行为可能不理想
     */
    override fun toString(): String = "RelativeFileScope(internal=\"$internalPathString\")"

    companion object
    {
        internal val EMPTY_Relative_File_Scope = RelativeScope("")

        fun create(vararg parts: String): RelativeScope
        {
            if (parts.all { it.isEmpty() }) return EMPTY_Relative_File_Scope
            return RelativeScope(PathUtil.combine("", *parts))
        }
    }
}

/**
 * 代表一个相对路径片段/作用域下的一个文件。
 */
@ConsistentCopyVisibility
data class RelativeFilePath internal constructor(
    // 内部存储，不对外暴露为公共API
    private val internalPathString: String,
    private val fileName: String
)
{
    /**
     * toString 仅用于调试，因为 value class 的 toString 默认行为可能不理想
     */
    override fun toString(): String = "RelativeFileScope(internal=\"$internalPathString\" with file=\"$fileName\")"

    /**
     * 固定在某个绝对路径上
     */
    fun pinToAbsolute(absoluteScope: AbsoluteScopeWithFormat): String
    {
        return absoluteScope.createScope(internalPathString).resolveFile(fileName)
    }

    fun withNewFileName(newFileName: String): RelativeFilePath{
        return RelativeFilePath(internalPathString, newFileName)
    }

    companion object
    {

        fun create(fileName: String, vararg parts: String): RelativeFilePath
        {
            if (parts.all { it.isEmpty() }) return EMPTY_Relative_File_Scope.resolveFile(fileName)
            return RelativeFilePath(PathUtil.combine("", *parts), fileName)
        }
    }
}

/**
 * 代表一个“从 Mod 认知的根开始的完整路径”。
 * 使用 @JvmInline value class 来优化。
 */
@JvmInline
value class AbsoluteScope private constructor(
    // 此“Mod绝对路径”的字符串表示。
    internal val workingPath: String
)
{

    // 对于 value class, isEmpty 可以是计算属性
    val isEmpty: Boolean get() = workingPath.isEmpty() && this != EMPTY_ROOT_ANCHOR // 防止 EMPTY_ROOT_ANCHOR 被认为是空的

    fun createScope(vararg subPathParts: String): AbsoluteScope
    {
        if (subPathParts.all { it.isEmpty() }) return this
        return AbsoluteScope(PathUtil.combine(this.workingPath, *subPathParts))
    }

//    fun resolveFile(fileName: String): String
//    {
//        if (fileName.isEmpty()) return this.workingPath
//        return PathUtil.combine(this.workingPath, fileName)
//    }

    /**
     * toString 仅用于调试，因为 value class 的 toString 默认行为可能不理想
     */
    override fun toString(): String = "AbsoluteFileScope(\"$workingPath\")"

    companion object
    {
        fun create(vararg parts: String): AbsoluteScope
        {
            if (parts.all { it.isEmpty() }) return EMPTY_ROOT_ANCHOR
            return AbsoluteScope(PathUtil.combine("", *parts))
        }

        // 用于表示一个“空的”但已锚定的根，如果 modRootPathString 本身就是空
        private val EMPTY_ROOT_ANCHOR = AbsoluteScope("")
    }
}

/**
 * 使得 AbsoluteScope 携带一个后缀名信息
 */
fun AbsoluteScope.withFormat(format: String): AbsoluteScopeWithFormat
{
    return AbsoluteScopeWithFormat(this, format)
}

/**
 * 代表一个“从 Mod 认知的根开始的完整路径”。
 * 携带一个后缀名信息（不带“.”）
 */
@ConsistentCopyVisibility
data class AbsoluteScopeWithFormat internal constructor(
    // 此“Mod绝对路径”的字符串表示。
    private val absoluteScope: AbsoluteScope,
    private val fileFormat: String
)
{

    // 对于 value class, isEmpty 可以是计算属性
    val isEmpty: Boolean get() = absoluteScope.isEmpty

    fun createScope(vararg subPathParts: String): AbsoluteScopeWithFormat
    {
        return AbsoluteScopeWithFormat(this.absoluteScope.createScope(*subPathParts), fileFormat)
    }

    /**
     * 输入不带后缀名的文件名，得到路径
     */
    fun resolveFile(fileNamePure: String): String
    {
//        if (fileNamePure.isEmpty()) return this.absoluteScope.workingPath
        return PathUtil.combine(this.absoluteScope.workingPath, "$fileNamePure.$fileFormat")
    }

    /**
     * toString 仅用于调试，因为 value class 的 toString 默认行为可能不理想
     */
    override fun toString(): String = "AbsoluteScopeWithFileType(\"$absoluteScope\" with \"$fileFormat\" )"
}