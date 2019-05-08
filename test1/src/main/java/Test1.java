import com.alibaba.fastjson.JSON;

public class Test1 {

    public static void main(String[] args) {
        JSON.parse("{a:1}");
    }

    public static void parseJson(){
        JSON.parse("{a:1}");
        System.out.println("Test1.parseJson() pass");
    }
}
