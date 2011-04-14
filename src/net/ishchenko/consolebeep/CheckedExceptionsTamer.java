package net.ishchenko.consolebeep;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 14.04.11
 * Time: 4:28
 */

/**
 * Everyone loves checked exceptions
 */
public class CheckedExceptionsTamer {

    @SuppressWarnings({"unchecked"})
    public static <T extends Throwable> void tame(Throwable e) throws T {
        throw (T) e;
    }

}
