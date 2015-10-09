/*
 * 
 *
 * 
 * 
 */
package com.fluxchess.pulse;

public final class Main {

  public static void main(String[] args) {
    // Don't do any fancy stuff here. Just create our engine and
    // run it. JCPI takes care of the rest. It waits for the GUI
    // to issue commands which will call our methods using the
    // visitor pattern.
    try {
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("perft")) {
          new Perft().run();
        }
      } else {
        new SimpleChessAI().run();
      }
    } catch (Throwable t) {
      System.out.format("Exiting due to an exception: %s%n", t.getLocalizedMessage());
      t.printStackTrace();
      System.exit(1);
    }
  }

}
