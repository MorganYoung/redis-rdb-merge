package morgan.learn;

import morgan.learn.rdb.RdbDescConstant;
import morgan.learn.rdb.RdbVersions;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class RedisRdbMerge  {
    public static void main( String[] args ) {
        System.out.println( "redis rdb merge" );

        printHexStr(RdbDescConstant.RDB_TITLE);
        printHexStr(RdbVersions.RDB_0002);
        printHexStr(RdbVersions.RDB_0006);
    }

    static void printHexStr(byte[] bytes) {
        for (byte b : bytes) {
            System.out.print(toHexStr(b));
        }
        System.out.println();
    }

    static String toHexStr(byte b) {
        return Integer.toHexString(b);
    }
}
