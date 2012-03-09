package woko.tooling.utils

class AppUtils {

    static String simpleAsk(String question){
        print "> $question :"
        DataInputStream stream = new DataInputStream(System.in);
        return stream.readLine()
    }
    
    static requiredAsk(String question){
        String response
        while(!response){
            print "> $question :"
            DataInputStream stream = new DataInputStream(System.in);
            response = stream.readLine()
        }
        return response
    }

    static String askWithDefault(String question, String defaultValue){
        print "> $question [$defaultValue] :"
        DataInputStream stream = new DataInputStream(System.in);
        String response = stream.readLine()
        return response ? response : defaultValue
    }

    static Boolean yesNoAsk(String question){
        print "> $question ? [y] :"
        DataInputStream stream = new DataInputStream(System.in);
        def response = stream.readLine()
        return response?.toLowerCase() != 'n'
    }

    static def extractPkgAndClazz(String fqcn) {
        int i = fqcn.lastIndexOf(".")
        if (i!=-1) {
            return [pkg:fqcn[0..i-1], clazz:fqcn[i+1..-1]]
        }
        return [pkg:'',clazz:fqcn]
    }

}
