package com.github.atomicblom.hcmw.client.model.obj;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.List;
import java.util.Map;

/**
 * Created by codew on 23/12/2016.
 */
@Deprecated
public class OBJState implements IModelState {
    protected Map<String, Boolean> visibilityMap = Maps.newHashMap();
    public IModelState parent;
    protected Operation operation = Operation.SET_TRUE;

    public OBJState(List<String> visibleGroups, boolean visibility) {
        this(visibleGroups, visibility, TRSRTransformation.identity());
    }

    public OBJState(List<String> visibleGroups, boolean visibility, IModelState parent) {
        this.parent = parent;
        for (String s : visibleGroups) this.visibilityMap.put(s, visibility);
    }

    public IModelState getParent(IModelState parent) {
        if (parent == null) return null;
        else if (parent instanceof OBJState) return ((OBJState) parent).parent;
        return parent;
    }

    public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part) {
        if (parent != null) return parent.apply(part);
        return Optional.absent();
    }

    public Map<String, Boolean> getVisibilityMap() {
        return this.visibilityMap;
    }

    public List<String> getGroupsWithVisibility(boolean visibility) {
        List<String> ret = Lists.newArrayList();
        for (Map.Entry<String, Boolean> e : this.visibilityMap.entrySet()) {
            if (e.getValue() == visibility) {
                ret.add(e.getKey());
            }
        }
        return ret;
    }

    public List<String> getGroupNamesFromMap() {
        return Lists.newArrayList(this.visibilityMap.keySet());
    }

    public void changeGroupVisibilities(List<String> names, Operation operation) {
        if (names == null || names.isEmpty()) return;
        this.operation = operation;
        if (names.get(0).equals(Group.ALL)) {
            for (String s : this.visibilityMap.keySet()) {
                this.visibilityMap.put(s, this.operation.performOperation(this.visibilityMap.get(s)));
            }
        } else if (names.get(0).equals(Group.ALL_EXCEPT)) {
            for (String s : this.visibilityMap.keySet()) {
                if (!names.subList(1, names.size()).contains(s)) {
                    this.visibilityMap.put(s, this.operation.performOperation(this.visibilityMap.get(s)));
                }
            }
        } else {
            for (String s : names) {
                this.visibilityMap.put(s, this.operation.performOperation(this.visibilityMap.get(s)));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("OBJState: ");
        builder.append(String.format("%n    parent: %s%n", this.parent.toString()));
        builder.append(String.format("    visibility map: %n"));
        for (Map.Entry<String, Boolean> e : this.visibilityMap.entrySet()) {
            builder.append(String.format("        name: %s visible: %b%n", e.getKey(), e.getValue()));
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(visibilityMap, parent, operation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OBJState other = (OBJState) obj;
        return Objects.equal(visibilityMap, other.visibilityMap) &&
                Objects.equal(parent, other.parent) &&
                operation == other.operation;
    }

    public enum Operation {
        SET_TRUE,
        SET_FALSE,
        TOGGLE;

        Operation() {
        }

        public boolean performOperation(boolean valueToToggle) {
            switch (this) {
                default:
                case SET_TRUE:
                    return true;
                case SET_FALSE:
                    return false;
                case TOGGLE:
                    return !valueToToggle;
            }
        }
    }
}
