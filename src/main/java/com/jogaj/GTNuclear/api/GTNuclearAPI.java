package com.jogaj.GTNuclear.api;

import com.jogaj.GTNuclear.api.block.IModeratorType;
import com.jogaj.GTNuclear.common.block.ModeratorBlock;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Supplier;

public class GTNuclearAPI {
    public static final Map<IModeratorType, Supplier<ModeratorBlock>> MODERATORS = new HashMap<>();
}
