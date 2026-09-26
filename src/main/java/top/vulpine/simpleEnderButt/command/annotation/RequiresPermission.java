package top.vulpine.simpleEnderButt.command.annotation;

import revxrsal.commands.annotation.DistributeOnMethods;
import revxrsal.commands.annotation.NotSender;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a command as requiring a SimpleEnderButt permission node.
 * <p>
 * The value is relative to the plugin root, e.g. {@code command.reload} resolves
 * to {@code simpleenderbutt.command.reload} and is checked through
 * {@link top.vulpine.simpleEnderButt.util.PermissionChecker}, so wildcard and admin
 * nodes keep working.
 */
@DistributeOnMethods
@NotSender.ImpliesNotSender
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {

    /**
     * The permission node, relative to the {@code simpleenderbutt} root.
     *
     * @return The permission node
     */
    String value();

}
