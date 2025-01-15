package fr.nekotine.fniaf

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import fr.nekotine.core.ioc.Ioc
import fr.nekotine.fniaf.control.player.PlayerAnimatronicController
import fr.nekotine.fniaf.control.player.PlayerSurvivorController
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

class Commands {

    companion object{

        public fun registerCommands(){

            var fniafcommand = CommandAPICommand("fniaf");

            var start = CommandAPICommand("start")
                .executes(CommandExecutor { sender, args ->  Ioc.resolve(FnIAf::class.java).startGame()});

            var stop = CommandAPICommand("stop")
                .executes(CommandExecutor { sender, args ->  Ioc.resolve(FnIAf::class.java).stopGame()});

            var team  = CommandAPICommand("team")
                .executes(CommandExecutor { sender, args ->  sender.sendMessage("Usage: fniaf team <join/leave> ...")});

            var teamjoin = CommandAPICommand("join")
                .executes(CommandExecutor { sender, args ->  sender.sendMessage("Usage: fniaf team join <survivor/animatronic> <Player?>")});
            team.withSubcommand(teamjoin);

            var teamjoinsurvivor = CommandAPICommand("survivor")
                .executesPlayer(PlayerCommandExecutor { sender, args -> Ioc.resolve(FnIAf::class.java).joinSurvivor(sender)});
            teamjoin.withSubcommand(teamjoinsurvivor);

            var teamjoinsurvivorplayer = CommandAPICommand("survivor")
                .withArguments(EntitySelectorArgument.OnePlayer("player"))
                .executes(CommandExecutor { sender, args -> Ioc.resolve(FnIAf::class.java).joinSurvivor(args.get("player") as Player)});
            teamjoin.withSubcommand(teamjoinsurvivorplayer);

            var teamjoinanimatronic = CommandAPICommand("animatronic")
                .executesPlayer(PlayerCommandExecutor { sender, args -> Ioc.resolve(FnIAf::class.java).joinAnimatronic(sender)});
            teamjoin.withSubcommand(teamjoinanimatronic);

            var teamjoinanimatronicplayer = CommandAPICommand("animatronic")
                .withArguments(EntitySelectorArgument.OnePlayer("player"))
                .executesPlayer(PlayerCommandExecutor { sender, args -> run{
                    val player = args.get("player") as Player;
                    Ioc.resolve(FnIAf::class.java).joinAnimatronic(player);
                    sender.sendMessage(player.displayName().append(Component.text(" ajouté dans l'équipe animatronic",NamedTextColor.DARK_PURPLE)));
                }});
            teamjoin.withSubcommand(teamjoinanimatronicplayer);

            var teamleave = CommandAPICommand("leave")
                .executesPlayer(PlayerCommandExecutor { sender, args ->  Ioc.resolve(FnIAf::class.java).leaveGame(sender)})
                .executes(CommandExecutor{sender, args -> sender.sendMessage("Usage: fniaf team leave  <Player?>")});
            team.withSubcommand(teamleave);

            var teamleaveplayer = CommandAPICommand("leave")
                .withArguments(EntitySelectorArgument.OnePlayer("player"))
                .executes(CommandExecutor { sender, args ->  run{
                    val player = args.get("player") as Player;
                    Ioc.resolve(FnIAf::class.java).leaveGame(player);
                    sender.sendMessage(player.displayName().append(Component.text(" a été exclus de la partie",NamedTextColor.DARK_PURPLE)));
                }});
            team.withSubcommand(teamleaveplayer);

            var status = CommandAPICommand("status")
                .executes(CommandExecutor { sender, args -> run{
                    val game = Ioc.resolve(FnIAf::class.java)
                    sender.sendMessage(Component.text("Started: ${game.isRunning}"))

                    var animatronic = Component.text("Animatronics:").appendNewline()
                    game.animatronics.forEach{animatronic = animatronic.append(
                        Component.text(it.getName()+' ')
                        .append((it.controller as? PlayerAnimatronicController)?.player?.displayName()?:Component.empty())
                        .appendNewline())}
                    animatronic = animatronic.color(NamedTextColor.RED)
                    sender.sendMessage(animatronic)

                    var survivor = Component.text("Survivors:").appendNewline()
                    game.survivors.forEach{survivor = survivor.append(
                        (it.controller as? PlayerSurvivorController)?.player?.displayName()?.appendNewline() ?: Component.newline())}
                    survivor = survivor.color(NamedTextColor.BLUE)
                    sender.sendMessage(survivor)
                }});

            fniafcommand.withSubcommand(start);
            fniafcommand.withSubcommand(stop);
            fniafcommand.withSubcommand(team);
            fniafcommand.withSubcommand(status);

            fniafcommand.register();

        }

    }

}