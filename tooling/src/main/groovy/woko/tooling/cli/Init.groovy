package woko.tooling.cli

import woko.tooling.utils.Logger
import woko.tooling.cli.commands.InitCmd


class Init {

    void run(Logger logger){
        new InitCmd(logger).execute()
    }

    public static void main(String[] args) {
        Writer out = new OutputStreamWriter(System.out)
        try {
            new Init().run(new Logger(out))
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            out.flush()
            out.close()
        }
    }
}
