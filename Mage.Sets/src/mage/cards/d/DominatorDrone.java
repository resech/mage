
package mage.cards.d;

import mage.MageInt;
import mage.abilities.common.EntersBattlefieldTriggeredAbility;
import mage.abilities.condition.Condition;
import mage.abilities.condition.common.PermanentsOnTheBattlefieldCondition;
import mage.abilities.effects.common.LoseLifeOpponentsEffect;
import mage.abilities.keyword.DevoidAbility;
import mage.abilities.keyword.IngestAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.filter.FilterPermanent;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.predicate.mageobject.AnotherPredicate;
import mage.filter.predicate.mageobject.ColorlessPredicate;

import java.util.UUID;

/**
 * @author LevelX2
 */
public final class DominatorDrone extends CardImpl {

    private static final FilterPermanent filter = new FilterControlledCreaturePermanent("you control another colorless creature");

    static {
        filter.add(AnotherPredicate.instance);
        filter.add(ColorlessPredicate.instance);
    }

    private static final Condition condition = new PermanentsOnTheBattlefieldCondition(filter);

    public DominatorDrone(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{2}{B}");
        this.subtype.add(SubType.ELDRAZI);
        this.subtype.add(SubType.DRONE);
        this.power = new MageInt(3);
        this.toughness = new MageInt(2);

        // Devoid
        this.addAbility(new DevoidAbility(this.color));

        // Ingest (Whenever this creature deals combat damage to a player, that player exiles the top card of their library.)
        this.addAbility(new IngestAbility());

        // When Dominator Drone enters the battlefield, if you control another colorless creature, each opponent loses 2 life.
        this.addAbility(new EntersBattlefieldTriggeredAbility(new LoseLifeOpponentsEffect(2)).withInterveningIf(condition));
    }

    private DominatorDrone(final DominatorDrone card) {
        super(card);
    }

    @Override
    public DominatorDrone copy() {
        return new DominatorDrone(this);
    }
}
