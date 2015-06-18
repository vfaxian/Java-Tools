
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    public RegexTest() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String s = "123 efg(567)";
        Matcher m = Pattern.compile("^(?:(\\d+)\\s+(?:(\\S+)\\s)?)?([^(]+\\([^)]*\\))$").matcher(s);
        while(m.find()) {
            if(m.group(1) != null)
                System.out.println(m.group(1));
            if(m.group(2) != null)
                System.out.println(m.group(2));
            if(m.group(3) != null)
                System.out.println(m.group(3));
        }

        //匹配字符从开始到
//        s = "KK-3.5-YUKON-WUKONG-140509-0542";
//        s = "JB-MR2-VISKAN-140508-0218";
//        s = "BLUE-3.2-R1-MR-140417-0852";
        s = "L-MR1-KITAKAMI2-150413-2145";

        m = Pattern.compile("^(.*([a-zA-Z]|[a-zA-Z]\\d))+-").matcher(s);
        while(m.find()) {
            System.out.println(m.group(1));
        }

        s = "27.1.A.1.70_debug_2015-05-11-08-47-19_INTERNAL: 20150512052653: 20150512053323: Success";

        m = Pattern.compile("(_debug.*)$").matcher(s);
        while(m.find()) {
            System.out.println(m.group(1));
        }
        
        /**
         * Java中用正则表达式截取字符串中第一个出现的英文左括号之前的字符串。
         * 比如：北京市（海淀区）（朝阳区）（ 西城区），截取结果为：北京市。
         */
        String text = "北京市(海淀区)(朝阳区)(西城区)";

        //前面的.*?是非贪婪匹配的意思， 表示找到最小的就可以了
        //(?=Expression) 顺序环视，(?=\\()就是匹配正括号
        Pattern pattern = Pattern.compile(".*?(?=\\()");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
        }
    }

}
