package com.james.common.aux.converter;/*
 * Author: park.yq@alibaba-inc.com
 * Date: 2018/9/30 下午2:20
 */


import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelDistributionTraitDef;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.logical.LogicalFilter;

import com.james.common.aux.rel.DogFilter;
import com.james.common.aux.rel.DogRel;

public class DogFilterConverter extends ConverterRule {

	public static final DogFilterConverter INSTANCE = new DogFilterConverter(
			LogicalFilter.class,
			Convention.NONE,
			DogRel.CONVENTION,
			"DogFilterConverter"
	);
	public DogFilterConverter(Class<? extends RelNode> clazz, RelTrait in, RelTrait out, String description) {
		super(clazz, in, out, description);
	}

	@Override
	public boolean matches(RelOptRuleCall call) {
		return super.matches(call);
	}

	@Override
	public RelNode convert(RelNode rel) {
		LogicalFilter filter = (LogicalFilter) rel;
		RelNode input = convert(filter.getInput(), filter.getInput().getTraitSet().replace(DogRel.CONVENTION).simplify());
		return new DogFilter(
				filter.getCluster(),
				RelTraitSet.createEmpty().plus(DogRel.CONVENTION).plus(RelDistributionTraitDef.INSTANCE.getDefault()),
				input,
				filter.getCondition()
		);
	}


}
