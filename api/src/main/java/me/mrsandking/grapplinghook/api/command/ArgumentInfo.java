package me.mrsandking.grapplinghook.api.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface ArgumentInfo {

    /**
     * Description should be simple and short.
     * This is mostly used by help commands, etc.
     * @return argument description
     */

    String description();

    /**
     * This returns the permission that is needed to use sub-command
     * @return permission
     */

    String permission() default "grapplinghook.default";

    /**
     * This represents the information which tells if sub-command may be executed
     * only by player or not.
     * @return boolean value
     */

    boolean useByPlayer() default false;

    /**
     * This is used to make arguments flexible - for example giving some code,
     * player's user name, etc.
     * @return true/false
     */

    boolean flexibleArguments() default false;

}