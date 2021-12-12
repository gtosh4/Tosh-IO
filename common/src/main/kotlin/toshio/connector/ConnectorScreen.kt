package toshio.connector

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.entity.player.Inventory
import toshio.ToshIO
import toshio.gui.Position
import toshio.gui.Size
import toshio.util.DirectionMap

@Environment(EnvType.CLIENT)
class ConnectorScreen(menu: ConnectorMenu?, inventory: Inventory?, component: Component?) :
    AbstractContainerScreen<ConnectorMenu>(menu, inventory, component) {

    companion object {
        val texture = ToshIO.id("textures/gui/container/connector.png")

        val buttonPos = DirectionMap(
            Position(46, 58), // down
            Position(27, 18), // up
            Position(61, 27), // north
            Position(12, 48), // south
            Position(8, 27), // west
            Position(64, 48), // east
        )

        val buttonSize = Size(12, 14)
    }

    private val topLeft get() = Position(leftPos, topPos)

    override fun init() {
        super.init()
        buttonPos.forEach { (dir, pos) ->
            addButton(ConnectorSideButton(menu, dir, pos+topLeft, buttonSize))
        }
    }

    override fun render(poseStack: PoseStack?, i: Int, j: Int, f: Float) {
        this.renderBackground(poseStack)
        super.render(poseStack, i, j, f)
        this.renderTooltip(poseStack, i, j)
    }

    override fun renderBg(poseStack: PoseStack?, f: Float, i: Int, j: Int) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        minecraft!!.textureManager.bind(texture)
        val k = (width - imageWidth) / 2
        val l = (height - imageHeight) / 2
        this.blit(poseStack, k, l, 0, 0, imageWidth, imageHeight)
    }

    override fun keyPressed(key: Int, b: Int, c: Int): Boolean {
        if (key == 256) {
            minecraft?.player?.closeContainer()
            return true
        }
        return super.keyPressed(key, b, c)
    }
}

class ConnectorSideButton(
    private val menu: ConnectorMenu,
    private val side: Direction,
    pos: Position,
    size: Size
) : AbstractButton(pos.x, pos.y, size.w, size.h, TranslatableComponent("gui.toshio.shortdir.${side.getName()}")) {

    private val selected get() = side == menu.direction

    override fun renderButton(poseStack: PoseStack?, i: Int, j: Int, f: Float) {
        if (!visible) return

        var color = 0xe0e0e0
        if (!selected) {
            color = -0x5f5f60
        } else if (isHovered()) {
            color = 0xffffa0
        }

        super.renderButton(poseStack, i, j, f)

        drawCenteredString(
            poseStack,
            Minecraft.getInstance().font,
            getText(),
            x + width / 2,
            y + (height - 8) / 2,
            color
        )
    }

    private fun getText(): Component = TranslatableComponent("gui.toshio.shortdir.${side.getName()}")

    override fun onPress() {
        menu.direction = side
    }

    override fun renderToolTip(poseStack: PoseStack?, i: Int, j: Int) {
        side.getName()
    }
}