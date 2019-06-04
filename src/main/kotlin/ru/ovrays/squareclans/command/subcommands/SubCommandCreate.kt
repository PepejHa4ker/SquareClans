package ru.ovrays.squareclans.command.subcommands

import ru.ovrays.squareclans.SquareClansPlugin
import ru.ovrays.squareclans.VaultHandler
import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.data.file.ConfigLoader
import ru.ovrays.squareclans.data.file.DataLoader
import ru.ovrays.squareclans.model.Clan
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ClanUtilities
import ru.ovrays.squareclans.util.ColorUtilities
import java.text.NumberFormat



object SubCommandCreate : SubCommand() {

    val format: NumberFormat = NumberFormat.getInstance().apply {
        this.isGroupingUsed = true
    }

    override val acceptAutomatic: Boolean = false
    override val playerOnly: Boolean = true
    override val memberRelation: MemberRelation = MemberRelation.NON_MEMBER
    override val clanPermission: Permission? = null
    override val aliases: Collection<String> = setOf("create")
    override val commandPermission: String = "SquareClans.command.create"
    override val description: String = "Создать новый клан. Стоимость - ${format.format(ConfigLoader.getConfig().clanCreateCost)}"
    override val usage: String = createUsage("create <название>")
    override fun execute(sender: WrappedSender, args: Array<String>) {

        if(args.size != 1)
            invalidArgsCountError(1, args.size)

        val name = args.first()

        val balance = VaultHandler.econ.getBalance(sender.sourcePlayer)
        val price = ConfigLoader.getConfig().clanCreateCost.toDouble()
        val isMoneyEnough = VaultHandler.econ.has(sender.sourcePlayer, price)

        if(!name.matches(Clan.namePattern)) {
            sender.error(ConfigLoader.getConfig().clanIsInvalid)
            return
        }

        if(ClanUtilities.getClan(name) != null) {
            sender.error("Клан с таким именем уже существует")
            return
        }

        if(!isMoneyEnough) {
            sender.error("Недостаточно средств &8(${balance.toString().removeSuffix(".0")}/${format.format(price)})")
            return
        }

        if(price.toInt() != 0) {
            val withdrawResult = VaultHandler.econ.withdrawPlayer(
                sender.sourcePlayer,
                ConfigLoader.getConfig().clanCreateCost.toDouble()
            )
            if (!withdrawResult.transactionSuccess()) {
                sender.error("Ошибка при списании средств: ${withdrawResult.errorMessage}")
                return
            }
            sender.sendMessage("&eС вашего аккаунта было списано: ${SubCommandPrefix.format.format(price)}$")
        }

        Clan.clans.add( Clan(name, sender.sourcePlayer.uniqueId))
        DataLoader.save()
        SquareClansPlugin.broadcastMessage("${sender.sourcePlayer.displayName}${ColorUtilities.GREEN} создает клан ${ColorUtilities.RED}$name")
    }
}