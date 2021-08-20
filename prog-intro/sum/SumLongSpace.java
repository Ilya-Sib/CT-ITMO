public class SumLongSpace {
    public static void main(String[] args) {
        Long res = 0L;
        String buff = "";
        for (String arg : args){
            for (char c : arg.toCharArray()){
                if (Character.isSpaceChar(c)){
                    if (!buff.equals("")){
                        res += Long.parseLong(buff);
                        buff = "";
                    }
                } else{
                    buff += c;
                }
            }
            if (!buff.equals("")){
                res += Long.parseLong(buff);
                buff = "";
            }
        }
        System.out.println(res);
    }
}