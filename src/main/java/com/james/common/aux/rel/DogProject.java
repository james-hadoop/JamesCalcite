package com.james.common.aux.rel;
/*
 * Author: park.yq@alibaba-inc.com
 * Date: 2018/9/30 下午2:26
 */

import java.util.List;

import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Project;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;

import com.james.common.aux.cost.DogRelMetadataQuery;

public class DogProject extends Project implements DogRel {
    private RelOptCost cost;

    public DogProject(RelOptCluster cluster, RelTraitSet traits, RelNode input, List<? extends RexNode> projects,
            RelDataType rowType) {
        super(cluster, traits, input, projects, rowType);
    }

    @Override
    public Project copy(RelTraitSet traitSet, RelNode input, List<RexNode> projects, RelDataType rowType) {
        return null;
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {

        // return super.computeSelfCost(planner, mq);

        if (cost != null) {
            return cost;
        }
        cost = DogRelMetadataQuery.INSTANCE.getCumulativeCost(this);
        return cost;
    }
}
