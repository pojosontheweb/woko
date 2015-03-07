package woko.tooling.cli.commands

import groovy.json.JsonSlurper
import woko.tooling.cli.Command
import woko.tooling.cli.Runner
import woko.tooling.utils.AppHttpClient
import woko.tooling.utils.AppUtils

/**
 * Created by vankeisb on 14/04/14.
 */
class ExecGroovyCmd extends Command{

    ExecGroovyCmd(Runner runner) {
        super(
                runner,
                "groovy",
                "execute a groovy script on the server",
                "<file> [url]",
                "Sends the Groovy code for exec on the server, and prints our the result."
        )
    }

    @Override
    def execute(List<String> args) {
        if (!args) {
            logger.error("invalid groovy command")
            return runner.help("groovy")
        }

        String url = "http://localhost:8080/$pomHelper.model.build.finalName"
        if (args.size()>1) {
            url = args[1]
        }

        AppHttpClient c = new AppHttpClient(logger, url, AppUtils.isBuiltInAuth(pomHelper))
        String username = "wdevel"
        String password = "wdevel"
        def httpParams = [
                'facet.code': new File(args[0]).text
        ]
        c.doWithLogin(username, password) {
            c.post("/groovy", httpParams) { String resp ->
                def json = new JsonSlurper().parseText(resp)
                println json.log
            }
        }

    }
}
