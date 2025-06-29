package mage.cards.m;

import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.Cost;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.DontUntapInControllersUntapStepAllEffect;
import mage.abilities.keyword.FlyingAbility;
import mage.abilities.triggers.BeginningOfUpkeepTriggeredAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.TargetController;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.AbilityPredicate;
import mage.filter.predicate.permanent.TappedPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.Target;
import mage.target.common.TargetControlledPermanent;
import mage.util.ManaUtil;

import java.util.UUID;

/**
 * @author spjspj
 */
public final class Mudslide extends CardImpl {

    private static final FilterCreaturePermanent filterCreature = new FilterCreaturePermanent("creatures without flying");

    static {
        filterCreature.add(Predicates.not(new AbilityPredicate(FlyingAbility.class)));
    }

    public Mudslide(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ENCHANTMENT}, "{2}{R}");

        // Creatures without flying don't untap during their controllers' untap steps.
        this.addAbility(new SimpleStaticAbility(new DontUntapInControllersUntapStepAllEffect(Duration.WhileOnBattlefield, TargetController.ANY, filterCreature)));

        // At the beginning of each player's upkeep, that player may choose any number of tapped creatures without flying they control and pay {2} for each creature chosen this way. If the player does, untap those creatures.
        this.addAbility(new BeginningOfUpkeepTriggeredAbility(TargetController.EACH_PLAYER, new MudslideEffect(), false));
    }

    private Mudslide(final Mudslide card) {
        super(card);
    }

    @Override
    public Mudslide copy() {
        return new Mudslide(this);
    }
}

class MudslideEffect extends OneShotEffect {

    private static final FilterControlledCreaturePermanent filter = new FilterControlledCreaturePermanent("tapped creature without flying");

    static {
        filter.add(Predicates.not(new AbilityPredicate(FlyingAbility.class)));
        filter.add(TappedPredicate.TAPPED);
    }

    MudslideEffect() {
        super(Outcome.Benefit);
        staticText = "that player may choose any number of tapped creatures without flying they control and pay {2} for each creature chosen this way. If the player does, untap those creatures.";
    }

    private MudslideEffect(final MudslideEffect effect) {
        super(effect);
    }

    @Override
    public MudslideEffect copy() {
        return new MudslideEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {

        Player player = game.getPlayer(getTargetPointer().getFirst(game, source));
        Permanent sourcePermanent = game.getPermanent(source.getSourceId());
        if (player != null && sourcePermanent != null) {
            int countBattlefield = game.getBattlefield().getAllActivePermanents(filter, game.getActivePlayerId(), game).size();
            while (player.canRespond() && countBattlefield > 0 && player.chooseUse(Outcome.Benefit, "Pay {2} and untap a tapped creature without flying under your control?", source, game)) {
                Target tappedCreatureTarget = new TargetControlledPermanent(filter);
                tappedCreatureTarget.withNotTarget(true);
                if (player.choose(Outcome.Untap, tappedCreatureTarget, source, game)) {
                    Cost cost = ManaUtil.createManaCost(2, false);
                    Permanent tappedCreature = game.getPermanent(tappedCreatureTarget.getFirstTarget());
                    if (tappedCreature != null && cost.pay(source, game, source, player.getId(), false)) {
                        tappedCreature.untap(game);
                    } else {
                        break;
                    }
                } else {
                    break;
                }
                countBattlefield = game.getBattlefield().getAllActivePermanents(filter, game.getActivePlayerId(), game).size();
            }
            return true;
        }
        return false;
    }
}
