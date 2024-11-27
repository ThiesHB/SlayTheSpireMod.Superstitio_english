package superstitioapi.utils

import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import kotlin.math.floor
import kotlin.math.min

object SpriteBatchExtensionForBladeGunner
{
    fun drawProgress(
        sb: SpriteBatch, region: TextureRegion, x: Float, y: Float, width: Float, height: Float, startDegree: Float,
        endDegree: Float
    )
    {
        var startDegree = startDegree
        var endDegree = endDegree
        check(sb.isDrawing) { "SpriteBatch.begin must be called before draw." }

        startDegree = clipDegree(startDegree + 45) - 45
        endDegree = clipDegree(endDegree - startDegree) + startDegree

        if (startDegree == endDegree)
        {
            sb.draw(region, x, y, width, height)
            return
        }

        var currentDegree = startDegree
        var nextDegree = -45f
        var rightTop = false
        do
        {
            nextDegree += 180f
            rightTop = !rightTop
            if (currentDegree >= nextDegree)
            {
                continue
            }
            if (rightTop)
            {
                drawProgressRightTop(
                    sb, region, x, y, width, height, currentDegree, min(nextDegree.toDouble(), endDegree.toDouble())
                        .toFloat()
                )
            }
            else
            {
                drawProgressLeftBottom(
                    sb, region, x, y, width, height, currentDegree, min(nextDegree.toDouble(), endDegree.toDouble())
                        .toFloat()
                )
            }
            currentDegree = nextDegree
        }
        while (nextDegree < endDegree)
    }

    private fun drawProgressRightTop(
        sb: SpriteBatch, region: TextureRegion, x: Float, y: Float, width: Float, height: Float, startDegree: Float,
        endDegree: Float
    )
    {
        var startDegree = startDegree
        var endDegree = endDegree
        startDegree = clipDegree(startDegree + 45) - 45
        endDegree = clipDegree(endDegree + 45) - 45

        val vertices = ReflectionHacks.getPrivate<FloatArray>(sb, SpriteBatch::class.java, "vertices")
        val texture = region.texture
        if (texture !== ReflectionHacks.getPrivate<Any>(sb, SpriteBatch::class.java, "lastTexture"))
        {
            ReflectionHacks.privateMethod(SpriteBatch::class.java, "switchTexture", Texture::class.java)
                .invoke<Any>(sb, texture)
        }
        else if (ReflectionHacks.getPrivate<Any>(sb, SpriteBatch::class.java, "idx") == vertices.size)
        {
            sb.flush()
        }

        val cx = x + width / 2
        val cy = y + height / 2
        val cu = (region.u + region.u2) / 2
        val cv = (region.v + region.v2) / 2

        val x1: Float
        val y1: Float
        val u1: Float
        val v1: Float
        val x3: Float
        val y3: Float
        val u3: Float
        val v3: Float

        if (endDegree > 45)
        {
            y1 = y + height
            x1 = cx + (y1 - cy) / MathUtils.cosDeg(90 - endDegree) * MathUtils.sinDeg(90 - endDegree)
            v1 = region.v2
            u1 = cu + (v1 - cv) / MathUtils.cosDeg(90 - endDegree) * MathUtils.sinDeg(90 - endDegree)
        }
        else
        {
            x1 = x + width
            y1 = cy + (x1 - cx) / MathUtils.cosDeg(endDegree) * MathUtils.sinDeg(endDegree)
            u1 = region.u2
            v1 = cv + (u1 - cu) / MathUtils.cosDeg(endDegree) * MathUtils.sinDeg(endDegree)
        }

        if (startDegree > 45)
        {
            y3 = y + height
            x3 = cx + (y3 - cy) / MathUtils.cosDeg(90 - startDegree) * MathUtils.sinDeg(90 - startDegree)
            v3 = region.v2
            u3 = cu + (v3 - cv) / MathUtils.cosDeg(90 - startDegree) * MathUtils.sinDeg(90 - startDegree)
        }
        else
        {
            x3 = x + width
            y3 = cy + (x3 - cx) / MathUtils.cosDeg(startDegree) * MathUtils.sinDeg(startDegree)
            u3 = region.u2
            v3 = cv + (u3 - cu) / MathUtils.cosDeg(startDegree) * MathUtils.sinDeg(startDegree)
        }

        val x2: Float
        val y2: Float
        val u2: Float
        val v2: Float

        if ((startDegree > 45) == (endDegree > 45))
        {
            x2 = x3
            y2 = y3
            u2 = u3
            v2 = v3
        }
        else
        {
            x2 = x + width
            y2 = y + height
            u2 = region.u2
            v2 = region.v2
        }

        val color = ReflectionHacks.getPrivate<Float>(sb, SpriteBatch::class.java, "color")
        val idx = ReflectionHacks.getPrivate<Int>(sb, SpriteBatch::class.java, "idx")
        vertices[idx] = cx
        vertices[idx + 1] = cy
        vertices[idx + 2] = color
        vertices[idx + 3] = cu
        vertices[idx + 4] = cv
        vertices[idx + 5] = x1
        vertices[idx + 6] = y1
        vertices[idx + 7] = color
        vertices[idx + 8] = u1
        vertices[idx + 9] = v1
        vertices[idx + 10] = x2
        vertices[idx + 11] = y2
        vertices[idx + 12] = color
        vertices[idx + 13] = u2
        vertices[idx + 14] = v2
        vertices[idx + 15] = x3
        vertices[idx + 16] = y3
        vertices[idx + 17] = color
        vertices[idx + 18] = u3
        vertices[idx + 19] = v3
        ReflectionHacks.setPrivate(sb, SpriteBatch::class.java, "idx", idx + 20)
    }

    private fun drawProgressLeftBottom(
        sb: SpriteBatch, region: TextureRegion, x: Float, y: Float, width: Float, height: Float, startDegree: Float,
        endDegree: Float
    )
    {
        var startDegree = startDegree
        var endDegree = endDegree
        startDegree = clipDegree(startDegree + 45) - 45
        endDegree = clipDegree(endDegree)

        val vertices = ReflectionHacks.getPrivate<FloatArray>(sb, SpriteBatch::class.java, "vertices")
        val texture = region.texture
        if (texture !== ReflectionHacks.getPrivate<Any>(sb, SpriteBatch::class.java, "lastTexture"))
        {
            ReflectionHacks.privateMethod(SpriteBatch::class.java, "switchTexture", Texture::class.java)
                .invoke<Any>(sb, texture)
        }
        else if (ReflectionHacks.getPrivate<Any>(sb, SpriteBatch::class.java, "idx") == vertices.size)
        {
            sb.flush()
        }

        val cx = x + width / 2
        val cy = y + height / 2
        val cu = (region.u + region.u2) / 2
        val cv = (region.v + region.v2) / 2

        val x1: Float
        val y1: Float
        val u1: Float
        val v1: Float
        val x3: Float
        val y3: Float
        val u3: Float
        val v3: Float

        if (endDegree > 225)
        {
            y1 = y
            x1 = cx + (y1 - cy) / MathUtils.cosDeg(270 - endDegree) * MathUtils.sinDeg(270 - endDegree)
            v1 = region.v
            u1 = cu + (v1 - cv) / MathUtils.cosDeg(270 - endDegree) * MathUtils.sinDeg(270 - endDegree)
        }
        else
        {
            x1 = x
            y1 = cy + (x1 - cx) / MathUtils.cosDeg(endDegree - 180) * MathUtils.sinDeg(endDegree - 180)
            u1 = region.u
            v1 = cv + (u1 - cu) / MathUtils.cosDeg(endDegree - 180) * MathUtils.sinDeg(endDegree - 180)
        }

        if (startDegree > 225)
        {
            y3 = y
            x3 = cx + (y3 - cy) / MathUtils.cosDeg(270 - startDegree) * MathUtils.sinDeg(270 - startDegree)
            v3 = region.v
            u3 = cu + (v3 - cv) / MathUtils.cosDeg(270 - startDegree) * MathUtils.sinDeg(270 - startDegree)
        }
        else
        {
            x3 = x
            y3 = cy + (x3 - cx) / MathUtils.cosDeg(startDegree - 180) * MathUtils.sinDeg(startDegree - 180)
            u3 = region.u
            v3 = cv + (u3 - cu) / MathUtils.cosDeg(startDegree - 180) * MathUtils.sinDeg(startDegree - 180)
        }

        val x0: Float
        val y0: Float
        val u0: Float
        val v0: Float

        if ((startDegree > 225) == (endDegree > 225))
        {
            x0 = x3
            y0 = y3
            u0 = u3
            v0 = v3
        }
        else
        {
            x0 = x
            y0 = y
            u0 = region.u
            v0 = region.v
        }

        val color = ReflectionHacks.getPrivate<Float>(sb, SpriteBatch::class.java, "color")
        val idx = ReflectionHacks.getPrivate<Int>(sb, SpriteBatch::class.java, "idx")
        vertices[idx] = x0
        vertices[idx + 1] = y0
        vertices[idx + 2] = color
        vertices[idx + 3] = u0
        vertices[idx + 4] = v0
        vertices[idx + 5] = x1
        vertices[idx + 6] = y1
        vertices[idx + 7] = color
        vertices[idx + 8] = u1
        vertices[idx + 9] = v1
        vertices[idx + 10] = cx
        vertices[idx + 11] = cy
        vertices[idx + 12] = color
        vertices[idx + 13] = cu
        vertices[idx + 14] = cv
        vertices[idx + 15] = x3
        vertices[idx + 16] = y3
        vertices[idx + 17] = color
        vertices[idx + 18] = u3
        vertices[idx + 19] = v3
        ReflectionHacks.setPrivate(sb, SpriteBatch::class.java, "idx", idx + 20)
    }

    private fun clipDegree(degree: Float): Float
    {
        var degree = degree
        if (degree < 0)
        {
            degree += (360 * (1 + floor((-degree / 360).toDouble()).toInt())).toFloat()
        }
        else if (degree >= 360)
        {
            degree = degree % 360
        }
        return degree
    }
}
