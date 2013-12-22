package org.datagr4m.application.neo4j.workspace;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.datagr4m.utils.IParserHelper;
import org.jzy3d.io.SimpleFile;


public class DatabaseConfiguration implements IParserHelper{
    public static String readDatabase(Neo4jWorkspaceSettings settings) throws IOException {
        String s = settings.getLocalDataPath();
        Logger.getLogger(DatabaseConfiguration.class).info("looking for config file:" + s);
        List<String> lines = SimpleFile.read(s);
        
        for(String line: lines){
            Matcher m = dataPattern.matcher(line);
            if(m.matches()){
                return m.group(1);
            }
        }
        return null;
    }

    static Pattern dataPattern = Pattern.compile(spnt + "database" + spnt + "=" + spnt + "(" + path + ")" + spnt);
}

    