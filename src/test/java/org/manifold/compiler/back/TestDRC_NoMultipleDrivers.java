package org.manifold.compiler.back;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.manifold.compiler.ConnectionValue;
import org.manifold.compiler.NodeValue;
import org.manifold.compiler.back.digital.DRC_NoMultipleDrivers;
import org.manifold.compiler.back.digital.DesignRuleCheck;
import org.manifold.compiler.back.digital.Netlist;
import org.manifold.compiler.middle.Schematic;
import org.manifold.compiler.middle.SchematicException;

@RunWith(Parameterized.class)
public class TestDRC_NoMultipleDrivers {

  @BeforeClass
  public static void setupClass() {
    UtilSchematicConstruction.setupIntermediateTypes();
  }

  @Parameters
  public static Collection<Object[]> data() throws SchematicException {
    List<Object[]> testData = new LinkedList<>();

    // BEGIN CASE 0
    // |in0> --- <out0|
    // no multiple drivers: TRUE
    {
      Schematic case0 = UtilSchematicConstruction.instantiateSchematic("case0");
      NodeValue in0 = UtilSchematicConstruction.instantiateInputPin();
      case0.addNode("in0", in0);
      NodeValue out0 = UtilSchematicConstruction.instantiateOutputPin();
      case0.addNode("out0", out0);
      ConnectionValue in0_to_out0 = UtilSchematicConstruction.instantiateWire(
          in0.getPort("out"), out0.getPort("in"));
      case0.addConnection("in0_to_out0", in0_to_out0);

      Netlist netlist_case0 = new Netlist(case0);
      Object[] case0_data = new Object[] { netlist_case0, true };
      testData.add(case0_data);
    }
    // END CASE 0
    
    // BEGIN CASE 1
    // |in0> -+- <out0|
    // |in1> -|
    // no multiple drivers: FALSE
    // END CASE 1
    {
      Schematic case1 = UtilSchematicConstruction.instantiateSchematic("case1");
      NodeValue in0 = UtilSchematicConstruction.instantiateInputPin();
      case1.addNode("in0", in0);
      NodeValue in1 = UtilSchematicConstruction.instantiateInputPin();
      case1.addNode("in1", in1);
      NodeValue out0 = UtilSchematicConstruction.instantiateOutputPin();
      case1.addNode("out0", out0);
      ConnectionValue in0_to_out0 = UtilSchematicConstruction.instantiateWire(
          in0.getPort("out"), out0.getPort("in"));
      case1.addConnection("in0_to_out0", in0_to_out0);
      ConnectionValue in1_to_out0 = UtilSchematicConstruction.instantiateWire(
          in1.getPort("out"), out0.getPort("in"));
      case1.addConnection("in1_to_out0", in1_to_out0);

      Netlist netlist_case1 = new Netlist(case1);
      Object[] case1_data = new Object[] { netlist_case1, false };
      testData.add(case1_data);
    }
    return testData;
  }

  // test inputs
  private Netlist netlist;
  private boolean expectedCheckResult;

  public TestDRC_NoMultipleDrivers(Netlist netlist, 
      Boolean expectedCheckResult) {
    this.netlist = netlist;
    this.expectedCheckResult = expectedCheckResult;
  }

  @Test
  public void testDRC() {
    DesignRuleCheck drc = new DRC_NoMultipleDrivers(netlist);
    drc.check();
    boolean actualCheckResult = drc.passed();
    assertEquals(expectedCheckResult, actualCheckResult);
  }

}
