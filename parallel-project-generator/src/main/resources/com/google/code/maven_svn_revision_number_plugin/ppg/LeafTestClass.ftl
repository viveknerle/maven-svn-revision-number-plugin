package ${javaName};

import org.junit.Test;
import static org.junit.Assert.*;

public class ${javaName}Test {

    @Test
    public void test000() {
        ${javaName} object = new ${javaName}();
        assertEquals( "0+0=0", 0, object.sum( 0, 0 ) );
    }

    @Test
    public void test123() {
        ${javaName} object = new ${javaName}();
        assertEquals( "1+2=3", 3, object.sum( 1, 2 ) );
    }

    @Test
    public void test347() {
        ${javaName} object = new ${javaName}();
        assertEquals( "3+4=7", 7, object.sum( 3, 4 ) );
    }

}
