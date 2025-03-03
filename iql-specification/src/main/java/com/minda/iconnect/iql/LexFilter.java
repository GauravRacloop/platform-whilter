package com.minda.iconnect.iql;

import java.util.Collection;

public class LexFilter {

    private Node node;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public enum Type {
        RULE_UNIT, LOGIC_GATE
    }

    public static class Node {
        private NodeData nodeData;
        private Node left;
        private Node right;

        public Node() {
        }

        public Node(Node left, NodeData nodeData, Node right) {
            this.left = left;
            this.nodeData = nodeData;
            this.right = right;
        }

        public NodeData getNodeData() {
            return nodeData;
        }

        public void setNodeData(NodeData nodeData) {
            this.nodeData = nodeData;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }
    }

    public static class NodeData {
        private RuleUnit ruleUnit;
        private LogicGate logicGate;
        private Type type;

        public NodeData() {
        }

        public NodeData(RuleUnit ruleUnit) {
            this.ruleUnit = ruleUnit;
            this.type = Type.RULE_UNIT;
        }

        public NodeData(LogicGate logicGate) {
            this.logicGate = logicGate;
            this.type = Type.LOGIC_GATE;
        }

        public RuleUnit getRuleUnit() {
            return ruleUnit;
        }

        public LogicGate getLogicGate() {
            return logicGate;
        }

        public Type getType() {
            return type;
        }
    }

}
