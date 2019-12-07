package cc.bohrx.promql.struct.modifier;

import static cc.bohrx.promql.struct.modifier.VectorMatchs.GroupTypeModifiers.GROUP_LEFT;
import static cc.bohrx.promql.struct.modifier.VectorMatchs.GroupTypeModifiers.GROUP_RIGHT;

public enum VectorMatchs {
    IGNORING("ignoring"),
    ON("on");

    private final String modifierName;

    public String getModifierName() {
        return modifierName;
    }

    VectorMatchs(String modifierName) {
        this.modifierName = modifierName;
    }

    public VectorMatchModifier.Builder get() {
        return new VectorMatchModifier.Builder(this);
    }

    public VectorMatchModifier.Builder groupLeft() {
        return new VectorMatchModifier.Builder(this).setGroup(GROUP_LEFT);
    }

    public VectorMatchModifier.Builder groupRight() {
        return new VectorMatchModifier.Builder(this).setGroup(GROUP_RIGHT);
    }

    enum GroupTypeModifiers {
        GROUP_LEFT("group_left"), GROUP_RIGHT("group_right");

        public String getGroupType() {
            return groupType;
        }

        private final String groupType;


        GroupTypeModifiers(String groupType) {
            this.groupType = groupType;
        }
    }
}
