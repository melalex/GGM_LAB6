package ua.room414;

import javax.vecmath.*;
import javax.media.j3d.*;

import com.sun.j3d.utils.behaviors.vp.*;

import javax.swing.JFrame;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import java.awt.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class Main extends JFrame {
    private Canvas3D myCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

    private void render() throws IOException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SimpleUniverse simpUniv = new SimpleUniverse(myCanvas3D);
        simpUniv.getViewingPlatform().setNominalViewingTransform();
        this.createSceneGraph(simpUniv);

        BranchGroup bgLight = new BranchGroup();
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Color3f lightColour1 = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f lightDir1 = new Vector3f(-1.0f, 0.0f, -0.5f);
        DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
        light1.setInfluencingBounds(bounds);
        bgLight.addChild(light1);
        simpUniv.addBranchGraph(bgLight);

        OrbitBehavior ob = new OrbitBehavior(myCanvas3D);
        ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE));
        simpUniv.getViewingPlatform().setViewPlatformBehavior(ob);


        this.setTitle("Warrior");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        this.getContentPane().add("Center", myCanvas3D);
        this.setVisible(true);
    }

    private void createSceneGraph(SimpleUniverse simpUniv) throws IOException {
        ObjectFile f = new ObjectFile(ObjectFile.RESIZE);
        Scene trainerScene = f.load("warrior.obj");

        Hashtable warriorNamedObjects = trainerScene.getNamedObjects();
        Enumeration keys = warriorNamedObjects.keys();

        while (keys.hasMoreElements()) {
            String name = (String) keys.nextElement();
            System.out.println("Name: " + name);
        }

        Transform3D trWarrior = new Transform3D();
        trWarrior.setScale(0.5f);
        TransformGroup tgWarrior = new TransformGroup(trWarrior);

        Shape3D head = (Shape3D) warriorNamedObjects.get("head");
        head.setAppearance(skinAppearance());

        Shape3D leftHand = (Shape3D) warriorNamedObjects.get("left_hand");
        leftHand.setAppearance(skinAppearance());

        Shape3D rightHand = (Shape3D) warriorNamedObjects.get("right_hand");
        rightHand.setAppearance(skinAppearance());

        Shape3D torso = (Shape3D) warriorNamedObjects.get("group1_____02");
        torso.setAppearance(armorAppearance());

        Shape3D downPart = (Shape3D) warriorNamedObjects.get("group1_____01");
        downPart.setAppearance(armorAppearance());

        tgWarrior.addChild(head.cloneTree());
        tgWarrior.addChild(leftHand.cloneTree());
        tgWarrior.addChild(rightHand.cloneTree());
        tgWarrior.addChild(torso.cloneTree());
        tgWarrior.addChild(downPart.cloneTree());

        Shape3D axe = (Shape3D) warriorNamedObjects.get("box02_group1");
        axe.setAppearance(axeAppearance());

        TransformGroup tgAxe = new TransformGroup();
        tgAxe.addChild(transformNode(
                axe.cloneTree(),
                new Vector3d(0.19, -0.45, 0),
                createRotationMatrix(0, -Math.PI / 2, -Math.PI / 2)
        ));

        Transform3D minPosition = new Transform3D();
        minPosition.rotZ(-Math.PI / 2);
        Alpha minPositionAlpha =
                new Alpha(1, Alpha.INCREASING_ENABLE, 1000, 0, 2000, 0, 0, 0, 0, 0);

        PositionInterpolator minArrRotation = new PositionInterpolator(
                minPositionAlpha,
                tgAxe,
                minPosition,
                0f,
                -0.5f
        );

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        minArrRotation.setSchedulingBounds(bounds);
        tgAxe.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgAxe.addChild(minArrRotation);
        tgWarrior.addChild(tgAxe);

        BranchGroup theScene = new BranchGroup();
        theScene.addChild(tgWarrior);

        TextureLoader t = new TextureLoader("fon.jpg", myCanvas3D);
        Background bg = new Background(t.getImage());
        bg.setImageScaleMode(Background.SCALE_FIT_ALL);
        BoundingSphere bounds8 = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        bg.setApplicationBounds(bounds8);
        theScene.addChild(bg);

        theScene.compile();
        simpUniv.addBranchGraph(theScene);
    }

    private static TransformGroup transformNode(Node obj, Vector3d pos, Matrix3d rotation) {
        TransformGroup transformGroup = new TransformGroup();
        Transform3D rotationTransform = new Transform3D();
        Transform3D moveTransform = new Transform3D();

        moveTransform.setTranslation(pos);
        rotationTransform.setRotation(rotation);
        moveTransform.mul(rotationTransform);

        transformGroup.setTransform(moveTransform);
        transformGroup.addChild(obj);

        return transformGroup;
    }

    private static Matrix3d createRotationMatrix(double x, double y, double z) {
        Matrix3d rotX = new Matrix3d();
        Matrix3d rotY = new Matrix3d();
        Matrix3d rotZ = new Matrix3d();

        rotX.rotX(x);
        rotY.rotY(y);
        rotZ.rotZ(z);

        rotX.mul(rotY);
        rotX.mul(rotZ);

        return rotX;
    }

    public static void main(String[] args) throws IOException {
        new Main().render();
    }

    private static Appearance skinAppearance() {
        Appearance ap = new Appearance();
        Color3f col = new Color3f(241f / 255, 194f / 255, 125f / 255);
        Material material = new Material(col, col, col, col, 100.0F);

        material.setLightingEnable(true);
        ap.setMaterial(material);

        addTexture(ap, "skin.jpg");

        return ap;
    }

    private static Appearance armorAppearance() {
        Appearance ap = new Appearance();
        Color3f col = new Color3f(84f / 255, 17f / 255, 1f / 255);
        Material material = new Material(col, col, col, col, 100.0F);

        material.setLightingEnable(true);
        ap.setMaterial(material);

        addTexture(ap, "armor.jpg");

        return ap;
    }

    private static Appearance axeAppearance() {
        Appearance ap = new Appearance();
        Color3f col = new Color3f(24f / 255, 24f / 255, 24f / 255);
        Material material = new Material(col, col, col, col, 100.0F);

        material.setLightingEnable(true);
        ap.setMaterial(material);

        addTexture(ap, "axe.jpg");

        return ap;
    }

    private static void addTexture(Appearance ap, String picture) {
        TextureLoader loader = new TextureLoader(picture, "LUMINANCE", new Container());
        Texture texture = loader.getTexture();

        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 1.0f, 0.0f));

        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);

        ap.setTexture(texture);
        ap.setTextureAttributes(texAttr);
    }

}