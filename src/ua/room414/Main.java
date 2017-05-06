package ua.room414;

import javax.vecmath.*;
import javax.media.j3d.*;

import com.sun.j3d.utils.behaviors.vp.*;

import javax.swing.JFrame;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

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
        this.setSize(700, 700);
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
        Appearance headApp = new Appearance();
        setToMyDefaultAppearance(headApp, new Color3f(0.5f, 0.2f, 0.4f));
        head.setAppearance(headApp);

        Shape3D leftHand = (Shape3D) warriorNamedObjects.get("left_hand");
        Appearance leftHandApp = new Appearance();
        setToMyDefaultAppearance(leftHandApp, new Color3f(1.0f, 0.9f, 0.2f));
        leftHand.setAppearance(leftHandApp);

        Shape3D rightHand = (Shape3D) warriorNamedObjects.get("right_hand");
        Appearance rightHandApp = new Appearance();
        setToMyDefaultAppearance(rightHandApp, new Color3f(1.0f, 0.9f, 0.2f));
        rightHand.setAppearance(rightHandApp);

        Shape3D torso = (Shape3D) warriorNamedObjects.get("group1_____02");
        Appearance torsoApp = new Appearance();
        setToMyDefaultAppearance(torsoApp, new Color3f(1.0f, 0.3f, 0.2f));
        torso.setAppearance(torsoApp);

        Shape3D downPart = (Shape3D) warriorNamedObjects.get("group1_____01");
        Appearance downPartApp = new Appearance();
        setToMyDefaultAppearance(downPartApp, new Color3f(1.0f, 0.3f, 0.2f));
        downPart.setAppearance(downPartApp);

        Shape3D axe = (Shape3D) warriorNamedObjects.get("box02_group1");
        Appearance axeApp = new Appearance();
        setToMyDefaultAppearance(axeApp, new Color3f(1.0f, 0.3f, 0.2f));
        axe.setAppearance(axeApp);

        Shape3D[] warrior = new Shape3D[]{
                head,
                leftHand,
                rightHand,
                torso,
                downPart,
                axe,
        };

        for (Shape3D shape : warrior) {
            tgWarrior.addChild(shape.cloneTree());
        }

        Shape3D body = (Shape3D) warriorNamedObjects.get("polygon0");
        Appearance bodyApp = new Appearance();
        setToMyDefaultAppearance(bodyApp, new Color3f(0.2f, 0.3f, 0.2f));
        body.setAppearance(bodyApp);

        TransformGroup tgBody = new TransformGroup();
        tgBody.addChild(body.cloneTree());

        Transform3D minArrowRotationAxis2 = new Transform3D();
        minArrowRotationAxis2.rotX(-Math.PI / 2);
        Alpha minRotationAlpha2 = new Alpha(1, Alpha.INCREASING_ENABLE, 1000, 0, 100, 0, 0, 0, 0, 0);
        RotationInterpolator minArrRotation2 = new RotationInterpolator(minRotationAlpha2, tgBody, minArrowRotationAxis2, 0.03f, 0.0f); //опис руху стрілки
        BoundingSphere bounds2 = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        minArrRotation2.setSchedulingBounds(bounds2);
        tgBody.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgBody.addChild(minArrRotation2);
        tgWarrior.addChild(tgBody);

        //////////////////////////////////////////////////////////
        Shape3D ball1 = (Shape3D) warriorNamedObjects.get("ball1");
        Appearance ballApp1 = new Appearance();
        setToMyDefaultAppearance(ballApp1, new Color3f(0.5f, 0.2f, 0.0f));
        ball1.setAppearance(ballApp1);

        TransformGroup tgBall1 = new TransformGroup();
        tgBall1.addChild(ball1.cloneTree());

        Transform3D minArrowRotationAxis1 = new Transform3D();
        minArrowRotationAxis1.rotY(Math.PI / 2);
        Alpha minRotationAlpha1 = new Alpha(1, Alpha.INCREASING_ENABLE, 1000, 0, 2000, 0, 0, 0, 0, 0);
        RotationInterpolator minArrRotation1 = new RotationInterpolator(minRotationAlpha1, tgBall1, minArrowRotationAxis1, (float) Math.PI, (float) Math.PI * 2); //опис руху стрілки
        BoundingSphere bounds1 = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        minArrRotation1.setSchedulingBounds(bounds1);
        tgBall1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgBall1.addChild(minArrRotation1);
        tgWarrior.addChild(tgBall1);

        ////////////////////////////////////////
        Shape3D ball = (Shape3D) warriorNamedObjects.get("ball2");
        Appearance ballApp = new Appearance();
        setToMyDefaultAppearance(ballApp, new Color3f(0.7f, 0.0f, 0.0f));
        ball.setAppearance(ballApp);

        TransformGroup tgBall = new TransformGroup();
        tgBall.addChild(ball.cloneTree());

        Transform3D minArrowRotationAxis = new Transform3D();
        minArrowRotationAxis.rotY(Math.PI / 2);
        Alpha minRotationAlpha = new Alpha(1, Alpha.INCREASING_ENABLE, 1000, 0, 2000, 0, 0, 0, 0, 0);
        RotationInterpolator minArrRotation = new RotationInterpolator(minRotationAlpha, tgBall, minArrowRotationAxis, (float) Math.PI, (float) Math.PI * 2); //опис руху стрілки
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        minArrRotation.setSchedulingBounds(bounds);
        tgBall.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgBall.addChild(minArrRotation);
        tgWarrior.addChild(tgBall);

        //////////////////////////////////////////
        Transform3D minArrowRotationAxis3 = new Transform3D();
        minArrowRotationAxis3.rotX(Math.PI / 2);
        Alpha minRotationAlpha3 = new Alpha(1, Alpha.INCREASING_ENABLE, 2900, 0, 100, 0, 0, 0, 0, 0);
        RotationInterpolator minArrRotation3 = new RotationInterpolator(minRotationAlpha3, tgBody, minArrowRotationAxis3, 0.0f, -0.03f); //опис руху стрілки
        BoundingSphere bounds3 = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        minArrRotation3.setSchedulingBounds(bounds3);
        tgBody.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgBody.addChild(minArrRotation3);

        BranchGroup theScene = new BranchGroup();
        theScene.addChild(tgWarrior);

        ////////////////////////////////////
        TextureLoader t = new TextureLoader("fon.jpg", myCanvas3D);
        Background bg = new Background(t.getImage());
        bg.setImageScaleMode(Background.SCALE_FIT_ALL);
        BoundingSphere bounds8 = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        bg.setApplicationBounds(bounds8);
        theScene.addChild(bg);

        theScene.compile();
        simpUniv.addBranchGraph(theScene);
    }

    public static void main(String[] args) throws IOException {
        new Main().render();
    }

    private static void setToMyDefaultAppearance(Appearance app, Color3f col) {
        app.setMaterial(new Material(col, col, col, col, 150.0f));
    }
}