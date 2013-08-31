package org.datagr4m.viewer.renderer.html;

import java.util.List;

public class Html {
    public static final int NO_BORDER = -1;
    
    public static String wrap(String html){
        return "<html><body>"+html+"</body></html>";
    }
    
    public static String table(List<String[]> lines){
        return table(lines, NO_BORDER);
    }
    
    public static String table(List<String[]> lines, int border){
        StringBuffer sb = new StringBuffer();
        
        if(border!=NO_BORDER)
            sb.append("<table border="+border+">");
        else
            sb.append("<table>");
        
        for (String[] line: lines) {
            sb.append("<tr>");
            
            for(String cell: line)
                sb.append("<td>"+cell+"</td>");
            
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
}
