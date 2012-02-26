package woko.tooling.utils

import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine

class AppUtils {

    static String simpleAsk(String question){
        print question
        DataInputStream stream = new DataInputStream(System.in);
        return stream.readLine()
    }
    
    static requiredAsk(String question){
        String response
        while(!response){
            print "$question :"
            DataInputStream stream = new DataInputStream(System.in);
            response = stream.readLine()
        }
        return response
    }

    static String askWithDefault(String question, String defaultValue){
        print "$question [$defaultValue] :"
        DataInputStream stream = new DataInputStream(System.in);
        String response = stream.readLine()
        return response ? response : defaultValue
    }

    static Boolean yesNoAsk(String question){
        print "$question ? [y] :"
        DataInputStream stream = new DataInputStream(System.in);
        def response = stream.readLine()
        return response?.toLowerCase() != 'n'
    }

    static generateTemplate(def props, String template, Writer writer){
        // Init velocity engine
        VelocityEngine ve = new VelocityEngine()
        Properties vProperties = new Properties()
        vProperties.load(VelocityEngine.class.getResourceAsStream('/velocity.properties'))
        ve.init(vProperties)

        // Grab template
        Template pomTemplate = ve.getTemplate("/templates/$template"+".vm")
        VelocityContext ctx = new VelocityContext()
        props.entrySet().each {
            ctx.put(it.key, it.value)
        }

        // Generate the file
        pomTemplate.merge(ctx, writer)
        writer.flush()
        writer.close()
    }




}
