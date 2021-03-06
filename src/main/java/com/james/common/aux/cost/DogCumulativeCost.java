package com.james.common.aux.cost;/*
                                  * Author: park.yq@alibaba-inc.com
                                  * Date: 2018/10/18 下午9:58
                                  */

import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.rel.metadata.BuiltInMetadata;
import org.apache.calcite.rel.metadata.MetadataDef;
import org.apache.calcite.rel.metadata.MetadataHandler;
import org.apache.calcite.rel.metadata.ReflectiveRelMetadataProvider;
import org.apache.calcite.rel.metadata.RelMetadataProvider;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.util.BuiltInMethod;

import com.james.common.aux.rel.DogFilter;
import com.james.common.aux.rel.DogProject;
import com.james.common.aux.rel.DogTableScan;

public class DogCumulativeCost implements MetadataHandler<BuiltInMetadata.CumulativeCost> {

    public static final RelMetadataProvider SOURCE = ReflectiveRelMetadataProvider
            .reflectiveSource(BuiltInMethod.CUMULATIVE_COST.method, new DogCumulativeCost());

    public DogCumulativeCost() {
    }

    @Override
    public MetadataDef<BuiltInMetadata.CumulativeCost> getDef() {
        return BuiltInMetadata.CumulativeCost.DEF;
    }

    public RelOptCost getCumulativeCost(DogTableScan tableScan, RelMetadataQuery mq) {
        return DogCostAccumulator.INSTACNE.getAccumulative(tableScan, mq);
    }

    public RelOptCost getCumulativeCost(DogProject dogProject, RelMetadataQuery mq) {
        return DogCostAccumulator.INSTACNE.getAccumulative(dogProject, mq);
    }

    public RelOptCost getCumulativeCost(DogFilter filter, RelMetadataQuery mq) {
        return DogCostAccumulator.INSTACNE.getAccumulative(filter, mq);
    }
}
