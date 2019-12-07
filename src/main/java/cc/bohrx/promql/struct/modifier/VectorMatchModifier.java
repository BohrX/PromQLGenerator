package cc.bohrx.promql.struct.modifier;

import cc.bohrx.promql.constant.Constant;
import cc.bohrx.promql.struct.StructTemplates;

public class VectorMatchModifier {

    private final VectorMatchs match;

    private final String[] matchLabels;

    private final VectorMatchs.GroupTypeModifiers group;

    private final String[] groupLabels;

    VectorMatchModifier(VectorMatchs match, String[] matchLabels, VectorMatchs.GroupTypeModifiers group, String[] groupLabels) {
        if (null == match) {
            throw new IllegalArgumentException("VectorMatchs can't be null ");
        }
        this.match = match;
        this.matchLabels = matchLabels;
        this.group = group;
        this.groupLabels = groupLabels;
    }

    public String print() {
        String result = Constant.SPACE;
        result += null == matchLabels ?
                match.getModifierName()
                : StructTemplates.printFunctionLikeStruct(match.getModifierName(), matchLabels);
        if (null != group) {
            result += Constant.SPACE + (null == groupLabels ?
                    group.getGroupType()
                    : StructTemplates.printFunctionLikeStruct(group.getGroupType(), groupLabels));
        }
        return result;
    }

    public static class Builder {
        private final VectorMatchs match;

        private String[] matchLabels;

        private VectorMatchs.GroupTypeModifiers group;

        private String[] groupLabels;

        public Builder(VectorMatchs match) {
            this.match = match;
        }


        public Builder setMatchLabels(String[] matchLabels) {
            this.matchLabels = matchLabels;
            return this;
        }

        public Builder setGroup(VectorMatchs.GroupTypeModifiers group) {
            this.group = group;
            return this;
        }

        public Builder setGroupLabels(String[] groupLabels) {
            this.groupLabels = groupLabels;
            return this;
        }

        public VectorMatchModifier build() {
            return new VectorMatchModifier(match, matchLabels, group, groupLabels);
        }
    }
}
