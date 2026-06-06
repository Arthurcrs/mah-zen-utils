package com.mahghuuuls.mahzenutils.compat.crafttweaker.stack;

import com.mahghuuuls.mahzenutils.common.log.RuntimeLog;
import com.mahghuuuls.mahzenutils.common.stack.StackExpirationRule;
import com.mahghuuuls.mahzenutils.common.stack.StackRefreshRule;
import com.mahghuuuls.mahzenutils.common.stack.StackService;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.mahzenutils.Stacks")
public class StackRegistrationZen {

    @ZenMethod
    public static boolean registerStack(String stackId,
                                        int maxStacks,
                                        int defaultDuration,
                                        String expirationRule,
                                        String refreshRule) {
        StackExpirationRule expiration = StackExpirationRule.fromZen(expirationRule);
        StackRefreshRule refresh = StackRefreshRule.fromZen(refreshRule);

        if (expiration == null) {
            RuntimeLog.invalidStackRule("expiration", expirationRule);
            return false;
        }

        if (refresh == null) {
            RuntimeLog.invalidStackRule("refresh", refreshRule);
            return false;
        }

        return StackService.register(stackId, maxStacks, defaultDuration, expiration, refresh);
    }
}