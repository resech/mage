package mage.cards.n;

import mage.MageInt;
import mage.abilities.condition.common.ThresholdCondition;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.common.ActivateIfConditionActivatedAbility;
import mage.abilities.effects.common.continuous.BecomesCreatureSourceEffect;
import mage.abilities.keyword.FirstStrikeAbility;
import mage.abilities.mana.ColorlessManaAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.AbilityWord;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.SubType;
import mage.game.permanent.token.TokenImpl;

import java.util.UUID;

/**
 * @author anonymous
 */
public final class NantukoMonastery extends CardImpl {

    public NantukoMonastery(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.LAND}, "");

        // {tap}: Add {C}.
        this.addAbility(new ColorlessManaAbility());

        // Threshold - {G}{W}: Nantuko Monastery becomes a 4/4 green and white Insect Monk creature with first strike until end of turn. It's still a land. Activate this ability only if seven or more cards are in your graveyard.
        this.addAbility(new ActivateIfConditionActivatedAbility(new BecomesCreatureSourceEffect(
                new NantukoMonasteryToken(), CardType.LAND, Duration.EndOfTurn),
                new ManaCostsImpl<>("{G}{W}"), ThresholdCondition.instance
        ).setAbilityWord(AbilityWord.THRESHOLD));
    }

    private NantukoMonastery(final NantukoMonastery card) {
        super(card);
    }

    @Override
    public NantukoMonastery copy() {
        return new NantukoMonastery(this);
    }
}

class NantukoMonasteryToken extends TokenImpl {

    public NantukoMonasteryToken() {
        super("", "4/4 green and white Insect Monk creature with first strike");
        cardType.add(CardType.CREATURE);
        subtype.add(SubType.INSECT);
        subtype.add(SubType.MONK);
        color.setGreen(true);
        color.setWhite(true);
        power = new MageInt(4);
        toughness = new MageInt(4);
        this.addAbility(FirstStrikeAbility.getInstance());
    }

    private NantukoMonasteryToken(final NantukoMonasteryToken token) {
        super(token);
    }

    public NantukoMonasteryToken copy() {
        return new NantukoMonasteryToken(this);
    }
}
