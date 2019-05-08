import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.spi.Module;

public class Test2 {
    public static void parseJson(){
        JSON.parse("{a:1}");
        new Module(){

            @Override
            public ObjectDeserializer createDeserializer(ParserConfig parserConfig, Class aClass) {
                return null;
            }

            @Override
            public ObjectSerializer createSerializer(SerializeConfig serializeConfig, Class aClass) {
                return null;
            }
        };
    }
}
