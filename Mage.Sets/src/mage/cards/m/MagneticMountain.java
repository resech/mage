package mage.cards.m;

import mage.ObjectColor;
import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.Cost;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.DontUntapInControllersUntapStepAllEffect;
import mage.abilities.triggers.BeginningOfUpkeepTriggeredAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.TargetController;
import mage.filter.FilterPermanent;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.mageobject.ColorPredicate;
import mage.filter.predicate.permanent.TappedPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.Target;
import mage.target.common.TargetControlledPermanent;
import mage.util.ManaUtil;

import java.util.UUID;

/**
 * @author MarcoMarin
 */
public final class MagneticMountain extends CardImpl {

    static final FilterPermanent filter = new FilterCreaturePermanent("blue creatures");

    static {
        filter.add(new ColorPredicate(ObjectColor.BLUE));
    }

    public MagneticMountain(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ENCHANTMENT}, "{1}{R}{R}");

        // Blue creatures don't untap during their controllers' untap steps.
        this.addAbility(new SimpleStaticAbility(new DontUntapInControllersUntapStepAllEffect(Duration.WhileOnBattlefield, TargetController.ANY, filter)));

        // At the beginning of each player's upkeep, that player may choose any number of tapped blue creatures they control and pay {4} for each creature chosen this way. If the player does, untap those creatures.
        this.addAbility(new BeginningOfUpkeepTriggeredAbility(TargetController.EACH_PLAYER, new MagneticMountainEffect(), false));
    }

    private MagneticMountain(final MagneticMountain card) {
        super(card);
    }

    @Override
    public MagneticMountain copy() {
        return new MagneticMountain(this);
    }
}

class MagneticMountainEffect extends OneShotEffect {

    private static final FilterControlledCreaturePermanent filter2 = new FilterControlledCreaturePermanent("tapped blue creature");

    static {
        filter2.add(new ColorPredicate(ObjectColor.BLUE));
        filter2.add(TappedPredicate.TAPPED);
    }

    MagneticMountainEffect() {
        super(Outcome.Benefit);
        staticText = "that player may choose any number of tapped blue creatures they control and pay {4} for each creature chosen this way. If the player does, untap those creatures.";
    }

    private MagneticMountainEffect(final MagneticMountainEffect effect) {
        super(effect);
    }

    @Override
    public MagneticMountainEffect copy() {
        return new MagneticMountainEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(game.getActivePlayerId());
        if (player == null) {
            return false;
        }
        int countBattlefield = game.getBattlefield().getAllActivePermanents(filter2, game.getActivePlayerId(), game).size();
        while (player.canRespond() && countBattlefield > 0 && player.chooseUse(Outcome.Benefit, "Pay {4} and untap a tapped blue creature under your control?", source, game)) {
            Target tappedCreatureTarget = new TargetControlledPermanent(filter2);
            tappedCreatureTarget.withNotTarget(true);
            if (player.choose(Outcome.Untap, tappedCreatureTarget, source, game)) {
                Cost cost = ManaUtil.createManaCost(4, false);
                Permanent tappedCreature = game.getPermanent(tappedCreatureTarget.getFirstTarget());
                if (tappedCreature != null && cost.pay(source, game, source, player.getId(), false)) {
                    tappedCreature.untap(game);
                } else {
                    break;
                }
            } else {
                break;
            }
            countBattlefield = game.getBattlefield().getAllActivePermanents(filter2, game.getActivePlayerId(), game).size();
        }
        return true;
    }
}
