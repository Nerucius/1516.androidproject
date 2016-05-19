package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.AnimatedSprite;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.TurretConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig;

/** A Generic turret for use with a {@link Vehicle}. */
public class Turret extends GameObject {
	private String id;

	public TurretConfig cfg = TurretConfig.DEFAULT;
	String model;
	AnimatedSprite sprite;
	Vector2 anchor;

	public Turret(String model, Vehicle owner, String anchor) {
		this.model = model;

		this.group = owner.group;
		this.anchor = owner.getAnchor(anchor);

		this.setParent(owner);
		this.setTag(owner.getTag() + "_turret");

		sprite = new AnimatedSprite(Assets.SPRITE_ATLAS.getRegions(model), 0);
		sprite.setFrameSpeed(0f);
		sprite.setSize(GameConfig.TILE_SIZE);
		sprite.setLooping(false);

		putComponent(new GraphicsComponent(sprite));
		putComponent(new PhysicsComponent());
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	@Override
	public void update(float delta) {
		getComponent(PhysicsComponent.class).setPosition(anchor);
		super.update(delta);
	}

	public void playFireAnimation(boolean looping) {
		sprite.setFrameTime(0.15f);
		sprite.setLooping(looping);
		sprite.play();
	}

	public void stopFireAnimation() {
		sprite.stop();
	}

}
