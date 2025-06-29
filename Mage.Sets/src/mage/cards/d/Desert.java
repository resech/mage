package mage.cards.d;

import mage.abilities.Ability;
import mage.abilities.condition.Condition;
import mage.abilities.condition.common.IsStepCondition;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.common.ActivateIfConditionActivatedAbility;
import mage.abilities.effects.common.DamageTargetEffect;
import mage.abilities.mana.ColorlessManaAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.PhaseStep;
import mage.constants.SubType;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.permanent.AttackingPredicate;
import mage.target.TargetPermanent;
import mage.target.common.TargetCreaturePermanent;

import java.util.UUID;

/**
 * @author LevelX2
 */
public final class Desert extends CardImpl {

    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("attacking creature");

    static {
        filter.add(AttackingPredicate.instance);
    }

    private static final Condition condition = new IsStepCondition(PhaseStep.END_COMBAT, false);

    public Desert(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.LAND}, "");
        this.subtype.add(SubType.DESERT);

        // {tap}: Add {C}.
        this.addAbility(new ColorlessManaAbility());

        // {tap}: Desert deals 1 damage to target attacking creature. Activate this ability only during the end of combat step.
        Ability ability = new ActivateIfConditionActivatedAbility(new DamageTargetEffect(1), new TapSourceCost(), condition);
        ability.addTarget(new TargetPermanent(filter));
        this.addAbility(ability);
    }

    private Desert(final Desert card) {
        super(card);
    }

    @Override
    public Desert copy() {
        return new Desert(this);
    }
}
