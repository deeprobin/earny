package de.deeprobin.earny.platform.sponge.command;

import de.deeprobin.earny.exception.ShorteningException;
import de.deeprobin.earny.platform.sponge.EarnyPlugin;
import de.deeprobin.earny.shorteners.IShortener;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.format.TextColors;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@RequiredArgsConstructor
public final class ShortUrlCommand implements CommandExecutor {

    private final EarnyPlugin plugin;

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        Optional<String> optional = args.getOne("type");
        Optional<String> optional2 = args.getOne("url");
        if(optional.isPresent() && optional2.isPresent()) {
            String type = optional.get();
            String url = optional2.get();

            IShortener shortener = this.plugin.getFactory().getShortenerManager().getShortenerByName(type, false);

            if(shortener == null) {
                source.sendMessage(Text.of(String.format("§cShortener %s is not available.", type.toUpperCase())));
                return CommandResult.empty();
            }
            source.sendMessage(Text.of("§aPlease wait. Generating shortened link..."));
            try {
                String result = shortener.shortUrl(url);
                Text.Builder builder = Text.builder().append(Text.of("Short URL: ")).color(TextColors.GOLD);
                builder.append(Text.of(result)).color(TextColors.YELLOW).onClick(ClickAction.OpenUrl.builder().url(new URL(result)).build());
                source.sendMessage(builder.build());
            } catch (ShorteningException | MalformedURLException e) {
                String haste = this.plugin.getFactory().getErrorReportUtil().getErrorReport(e);
                if(source.hasPermission("earny.admin")) {
                    source.sendMessage(Text.of("§cCannot short url. Please check the api key for this service. Stack Trace: " + this.plugin.getFactory().getErrorReportUtil().getErrorReport(e)));
                } else {
                    source.sendMessage(Text.of("§cSorry, an error occurred. Please contact an admin of this server."));
                }
                this.plugin.getLogger().warn(String.format("%s tried to short %s with the shortener %s. But there occurred an error. Please check your api keys or report this stacktrace: %s", source.getName(), type, url, haste));
                return CommandResult.empty();
            }
            return CommandResult.success();
        } else {
            source.sendMessage(Text.of("§cErrored"));
            return CommandResult.empty();
        }
    }
}
