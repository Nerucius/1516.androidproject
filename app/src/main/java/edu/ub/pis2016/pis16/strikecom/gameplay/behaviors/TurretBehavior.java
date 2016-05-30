package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Array;
import edu.ub.pis2016.pis16.strikecom.gameplay.Turret;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.TurretConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.factories.ProjectileFactory;

/**
 * Behavior of a generic turret.
 */
public class TurretBehavior extends BehaviorComponent {

	private GameObject target = null;
	private Vector2 tmp = new Vector2();

	private State state = State.IDLE;
	private float counter = 0;

	private float randomAngle = 0f;
	private enum State {
		IDLE,
		SEARCHING,
		AIMING,
	}

	@Override
	public void update(float delta) {
		counter += delta;
		TurretConfig cfg = ((Turret) gameObject).cfg;
		PhysicsComponent turretPhys = gameObject.getComponent(PhysicsComponent.class);
		PhysicsComponent vehiclePhys = gameObject.getParent().getComponent(PhysicsComponent.class);

		//turretPhys.setAngleLimits(minAngle + vehiclePhys.getRotation(), maxAngle + vehiclePhys.getRotation());

		switch (state) {
			case IDLE:
				// If we're idling, just look forward
				if (counter < cfg.idle_seconds) {
					tmp.set(8, 0).rotate(vehiclePhys.getRotation() + randomAngle).add(turretPhys.getPosition());
					turretPhys.lookAt(tmp, cfg.lerp_speed * .1f);

				} else {
					counter = 0;
					state = State.SEARCHING;
				}
				break;
			case SEARCHING:
				// Look for the closest gameobject in view
				GameObject.Faction faction = gameObject.faction;
				float closestDistance = Float.MAX_VALUE;
				GameObject closestGO = null;

				// It's fine to allocate a new Iterator here cause this will only be called once every second or so.
				Array.ArrayIterator<GameObject> iter = new Array.ArrayIterator<>(gameObject.getScreen().getGameObjects());
				while (iter.hasNext()) {
					GameObject go = iter.next();
					// Target enemies with the target tag, who are not too far, and are killable
					if (faction.isEnemy(go.faction) && go.killable && !isTooFar(go)) {
						float distance = go.getPosition().dst2(turretPhys.getPosition());
						if (distance < closestDistance) {
							closestGO = go;
							closestDistance = distance;
							// TODO Separate this into two classes, one for Enemy Turrets with DUMB AI, and one for StrikeBase Turrets
							break;
						}
					}
				}

				if (closestGO != null) {
					// If a target is found, aim at it immediately
					target = closestGO;
					state = State.AIMING;
				} else {
					// If none found, return to idle and move around
					state = State.IDLE;
					((Turret) gameObject).stopCannonAnimation();
					randomAngle = MathUtils.random(-180, 180);
				}
				break;
			case AIMING:
				if (!target.isValid() || isTooFar(target)) {
					target = null;
					state = State.SEARCHING;
				} else {
					float turretRot = turretPhys.getRotation();
					float targetAngle = tmp.set(target.getPosition()).sub(turretPhys.getPosition()).angle();

					// Move the turret towards the target position and check if it's within a 3 degree cone, shoot
					// a projectile towards it
					turretPhys.lookAt(target.getPosition(), cfg.lerp_speed);
					if (Math.abs(Angle.angleDelta(turretRot, targetAngle)) < cfg.fire_cone && counter > cfg.firerate) {
						shoot();
						counter = 0;
					}
				}
				break;
		}
	}

	private boolean isTooFar(GameObject other) {
		TurretConfig cfg = ((Turret) gameObject).cfg;

		if (!gameObject.hasComponent(PhysicsComponent.class))
			return true;

		tmp.set(gameObject.getPosition()).sub(other.getPosition());
		return tmp.len2() > cfg.range * cfg.range;
	}

	/** Override for aditional firing behavior */
	public void shoot() {
		// Play fire animation
		((Turret) gameObject).fireCannon(false);

		// Shooting mechanics
		TurretConfig cfg = ((Turret) gameObject).cfg;

		GameObject projectile = ProjectileFactory.newProjectile(cfg.proj_type);

		PhysicsComponent turretPhys = gameObject.getComponent(PhysicsComponent.class);
		PhysicsComponent projPhys = projectile.getComponent(PhysicsComponent.class);

		// Set position
		Sprite turretSprite = gameObject.getComponent(GraphicsComponent.class).getSprite();
		tmp.set(turretSprite.getSize() / 2f, 0).rotate(turretPhys.getRotation());
		tmp.add(turretPhys.getPosition());
		projPhys.setPosition(tmp);

		// Set the bullet spread here
		projPhys.setVelocity(tmp.set(cfg.proj_speed, 0).rotate(
				turretPhys.getRotation() + MathUtils.random(-1, 1) * cfg.fire_spread
		));
		projPhys.setRotation(turretPhys.getRotation());

		// set the tag to "player_proj" or "enemy_proj"
		projectile.setTag(gameObject.getParent().getTag() + "_proj");

		switch (gameObject.faction) {
			case PLAYER:
				projPhys.body.filter = Physics2D.Filter.PLAYER_PROJ;
				break;
			case RAIDERS:
				projPhys.body.filter = Physics2D.Filter.ENEMY_PROJ;
				break;
			default:
				projPhys.body.filter = Physics2D.Filter.ALL;
				break;
		}


		// set hitpoints as damage made on impact
		projectile.hitpoints = cfg.proj_damage;

		gameObject.getScreen().addGameObject(projectile);
	}
}