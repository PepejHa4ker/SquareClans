package ru.ovrays.squareclans.command.subcommands

import ru.ovrays.squareclans.command.wrapper.CommandError
import ru.ovrays.squareclans.command.wrapper.CommandSpec
import ru.ovrays.squareclans.command.wrapper.WrappedSender
import ru.ovrays.squareclans.model.Permission
import ru.ovrays.squareclans.util.ColorUtilities


abstract class SubCommand {
    enum class MemberRelation {
        ANY,
        NON_MEMBER,
        MEMBER
    }

    abstract val aliases: Collection<String>
    protected abstract val acceptAutomatic:  Boolean
    protected abstract val playerOnly: Boolean
    protected abstract val memberRelation: MemberRelation
    protected abstract val clanPermission: Permission?
    abstract val commandPermission: String
    abstract val description: String
    protected abstract fun execute(sender: WrappedSender, args: Array<String>)
    abstract val usage: String
    val helpEntry: String
            get() {
                return usage + ColorUtilities.DARK_GRAY + " - " + description
            }

    fun invalidArgsCountError(need: Int, spec: Int) {
        if(need == spec) return

        throw CommandError(
            if (need > spec) "Недостаточно аргументов" else "Слишком много аргументов"
        ,true)
    }

    protected fun createUsage(header: String) = ColorUtilities.RED +"/c " + header

    fun parseCommandSpec() : CommandSpec {
        return CommandSpec.builder()
                .permission(commandPermission)
                .description(description)
                .usage(usage)
                .executor { src, args ->
                    if(playerOnly) {
                        if(src.isPlayer) {
                            if(memberRelation == MemberRelation.MEMBER) {
                                if(src.sourcePlayer.isInClan()) {
                                    if( clanPermission != null) {
                                        if (!src.sourcePlayer.hasClanPermission(clanPermission!!)) {
                                            src.error("Вам требуется следующее право: ${clanPermission!!.getText()}")
                                            return@executor
                                        }
                                    }
                                } else {
                                    src.error(("Вы должны состоять в клане!"))
                                    return@executor
                                }
                            }
                            if (memberRelation == MemberRelation.NON_MEMBER) {
                                if(src.sourcePlayer.isInClan()) {
                                    src.error(("Вы не должны состоять в клане!"))
                                    return@executor
                                }
                            }
                        } else {
                            src.error(("Только от лица игрока!"))
                            return@executor
                        }
                    }
                    execute(src,args)
                    return@executor
                }
                .build()

    }
}