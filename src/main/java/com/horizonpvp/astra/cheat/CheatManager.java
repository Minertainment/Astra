package com.horizonpvp.astra.cheat;

import com.horizonpvp.astra.AntiCheat;
import com.horizonpvp.astra.cheat.block.FastBreak;
import com.horizonpvp.astra.cheat.combat.*;
import com.horizonpvp.astra.cheat.glitch.CreeperEgg;
import com.horizonpvp.astra.cheat.glitch.Enderpearl;
import com.horizonpvp.astra.cheat.movement.*;
import com.horizonpvp.astra.cheat.other.*;
import com.horizonpvp.astra.cheat.type.CheatType;

import java.util.HashMap;

public class CheatManager {

    private AntiCheat antiCheat;
    private HashMap<CheatType, Cheat> cheatMap = new HashMap<>();

    public CheatManager(AntiCheat antiCheat) {
        this.antiCheat = antiCheat;

        cheatMap.put(CheatType.AUTO_CLICKER, new AutoClicker(antiCheat));
        cheatMap.put(CheatType.ANGLE, new Angle(antiCheat));
        cheatMap.put(CheatType.CREEPER_EGG, new CreeperEgg(antiCheat));
        cheatMap.put(CheatType.CRITICALS, new Critical(antiCheat));
        cheatMap.put(CheatType.ANIMATION_CRASH, new Crash(antiCheat));
        cheatMap.put(CheatType.ENDERPEARL, new Enderpearl(antiCheat));
        cheatMap.put(CheatType.FAST_BOW, new FastBow(antiCheat));
        //cheatMap.put(CheatType.FAST_BREAK, new FastBreak(antiCheat));
        cheatMap.put(CheatType.FAST_EAT, new FastEat(antiCheat));
        cheatMap.put(CheatType.FAST_LADDER, new FastLadder(antiCheat));
        cheatMap.put(CheatType.FLY, new Fly(antiCheat));
        cheatMap.put(CheatType.FREECAM, new Freecam(antiCheat));
        cheatMap.put(CheatType.INVENTORY_MOVE, new InventoryMove(antiCheat));
        cheatMap.put(CheatType.INVENTORY_HIT, new InventoryHit(antiCheat));
        cheatMap.put(CheatType.JESUS, new Jesus(antiCheat));
        cheatMap.put(CheatType.KILL_AURA, new KillAura(antiCheat));
        cheatMap.put(CheatType.KNOCK_BACK, new Knockback(antiCheat));
        cheatMap.put(CheatType.MORE_PACKETS, new MorePackets(antiCheat));
        cheatMap.put(CheatType.NO_FALL, new NoFall(antiCheat));
        cheatMap.put(CheatType.NO_SLOW, new NoSlow(antiCheat));
        cheatMap.put(CheatType.NO_SWING, new NoSwing(antiCheat));
        cheatMap.put(CheatType.PHASE, new Phase(antiCheat));
        cheatMap.put(CheatType.REACH, new Reach(antiCheat));
        cheatMap.put(CheatType.REGEN, new Regen(antiCheat));
        cheatMap.put(CheatType.SNEAK_HIT, new Sneak(antiCheat));
        cheatMap.put(CheatType.SPEED, new Speed(antiCheat));
        cheatMap.put(CheatType.SPIDER, new Spider(antiCheat));
        cheatMap.put(CheatType.SPRINT, new Sprint(antiCheat));
        cheatMap.put(CheatType.STEP, new Step(antiCheat));
        cheatMap.put(CheatType.TIMER, new Timer(antiCheat));

    }

    public Cheat getCheat(CheatType type) {
        return cheatMap.get(type);
    }

}
