package mage.cards.e;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.PreventDamageByTargetEffect;
import mage.abilities.effects.common.PreventDamageToTargetEffect;
import mage.abilities.effects.common.UntapTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.SubType;
import mage.filter.FilterPermanent;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.predicate.permanent.AttackingPredicate;
import mage.target.TargetPermanent;

import java.util.UUID;

/**
 * @author LoneFox
 */
public final class ElvishScout extends CardImpl {

    private static final FilterPermanent filter = new FilterControlledCreaturePermanent("attacking creature you control");

    static {
        filter.add(AttackingPredicate.instance);
    }

    public ElvishScout(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{G}");
        this.subtype.add(SubType.ELF);
        this.subtype.add(SubType.SCOUT);
        this.power = new MageInt(1);
        this.toughness = new MageInt(1);

        // {G}, {tap}: Untap target attacking creature you control. Prevent all combat damage that would be dealt to and dealt by it this turn.
        Ability ability = new SimpleActivatedAbility(new UntapTargetEffect(), new ManaCostsImpl<>("{G}"));
        ability.addCost(new TapSourceCost());
        ability.addEffect(new PreventDamageByTargetEffect(Duration.EndOfTurn, true)
                .setText("Prevent all combat damage that would be dealt to"));
        ability.addEffect(new PreventDamageToTargetEffect(
                Duration.EndOfTurn, Integer.MAX_VALUE, true
        ).setText("and dealt by it this turn"));
        ability.addTarget(new TargetPermanent(filter));
        this.addAbility(ability);
    }

    private ElvishScout(final ElvishScout card) {
        super(card);
    }

    @Override
    public ElvishScout copy() {
        return new ElvishScout(this);
    }
}
