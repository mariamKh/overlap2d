/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.mvc.view;

import java.io.File;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.renderer.Overlap2dRenderer;
import com.uwsoft.editor.renderer.legacy.data.SceneVO;

public class Overlap2DScreen implements Screen, InputProcessor {
    private static final String TAG = Overlap2DScreen.class.getCanonicalName();
    //public SandboxStage sandboxStage;
    
    public UIStage uiStage;
    
	private Engine engine;
    
    private InputMultiplexer multiplexer;
    private Overlap2DFacade facade;
    private ProjectManager projectManager;
    private boolean paused = false;

    private Sandbox sandbox;

    public Overlap2DScreen() {
        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public void render(float deltaTime) {
        if (paused) {
            return;
        }
        GL20 gl = Gdx.gl;
        gl.glClearColor(0.129f, 0.129f, 0.129f, 1.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(deltaTime);
        

        uiStage.act(deltaTime);
        uiStage.draw();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {
        sandbox = Sandbox.getInstance();
        uiStage = sandbox.getUIStage();
        //sandboxStage = sandbox.getSandboxStage();
        
        //sandboxStage.sandbox = sandbox;

        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        // check for demo project
        File demoDir = new File(projectManager.getRootPath() + File.separator + "examples" + File.separator + "OverlapDemo");
        if (demoDir.isDirectory() && demoDir.exists()) {
            projectManager.openProjectFromPath(demoDir.getAbsolutePath() + File.separator + "project.pit");
            sandbox.loadCurrentProject();
            //uiStage.loadCurrentProject();
            //TODO set camer to it's place
            //renderer.viewPort.getCamera().position.set(400, 200, 0);
        }
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(uiStage);
        //TODO listeners 
        //multiplexer.addProcessor(sandboxStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void hide() {

    }

    @Override
    public void resize(int width, int height) {
        uiStage.resize(width, height);
        //TODO resize thing
        //sandboxStage.resize(width, height);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Gdx.input.isKeyPressed(Input.Keys.SYM) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            switch (keycode) {
                case Input.Keys.N:
                    //uiStage.menuMediator.showDialog("createNewProjectDialog");
                    break;
                case Input.Keys.O:
                    //uiStage.menuMediator.showOpenProject();
                    break;
                case Input.Keys.S:
                    SceneVO vo = sandbox.sceneVoFromItems();
                    projectManager.saveCurrentProject(vo);
                    break;
                case Input.Keys.E:
                    projectManager.exportProject();
                    break;
            }
        }

        Gdx.app.log(TAG, "keyDown : " + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

	public void setEngine(Engine engine) {
		this.engine = engine;
	}
}
