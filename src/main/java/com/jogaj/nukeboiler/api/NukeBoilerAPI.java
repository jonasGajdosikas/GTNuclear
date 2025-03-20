package com.jogaj.nukeboiler.api;

import com.jogaj.nukeboiler.api.block.IModeratorType;
import com.jogaj.nukeboiler.common.block.ModeratorBlock;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Supplier;

public class NukeBoilerAPI {

    public static final Map<IModeratorType, Supplier<ModeratorBlock>> MODERATORS = new HashMap<>();
}
