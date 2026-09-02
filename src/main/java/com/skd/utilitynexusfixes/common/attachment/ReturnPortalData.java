package com.skd.utilitynexusfixes.common.attachment;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReturnPortalData {

    private final Map<PortalKey, BlockPos> returnPortals = new HashMap<>();

    public void setReturnPortal(ResourceKey<Level> destinationDimension, BlockPos destinationPortalPos, BlockPos originPortalPos) {
        returnPortals.put(new PortalKey(destinationDimension, destinationPortalPos), originPortalPos.immutable());
    }

    public Optional<BlockPos> getReturnPortal(ResourceKey<Level> destinationDimension, BlockPos destinationPortalPos) {
        return Optional.ofNullable(returnPortals.get(new PortalKey(destinationDimension, destinationPortalPos)));
    }

    public void removeReturnPortal(ResourceKey<Level> destinationDimension, BlockPos destinationPortalPos) {
        returnPortals.remove(new PortalKey(destinationDimension, destinationPortalPos));
    }

    public void clear() {
        returnPortals.clear();
    }

    private record PortalKey(ResourceKey<Level> dimension, BlockPos pos) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PortalKey that)) return false;
            return dimension.equals(that.dimension) && pos.equals(that.pos);
        }

        @Override
        public int hashCode() {
            int result = dimension.hashCode();
            result = 31 * result + pos.hashCode();
            return result;
        }
    }
}
