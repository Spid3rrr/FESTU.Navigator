package com.example.festunavigator.data.pathfinding

import com.example.festunavigator.domain.pathfinding.Pathfinder
import com.example.festunavigator.domain.tree.Tree
import com.example.festunavigator.domain.tree.TreeNode

class AStarImpl: Pathfinder {

    override suspend fun findWay(from: String, to: String, tree: Tree): List<TreeNode>? {

        val finalNode = AStarNode(tree.entryPoints[to]!!, null)
        val initialNode = AStarNode(tree.entryPoints[from]!!, finalNode)

        val openList: MutableList<AStarNode> = mutableListOf()
        val closedSet: MutableSet<AStarNode> = mutableSetOf()

        openList.add(initialNode)

        while (openList.isNotEmpty()) {
            val currentNode = getNextAStarNode(openList)
            openList.remove(currentNode)
            closedSet.add(currentNode)

            if (currentNode == finalNode) {
                return getPath(currentNode).map { aStarNode ->
                    aStarNode.node
                }
            } else {
                addAdjacentNodes(currentNode, openList, closedSet, finalNode)
            }
        }
        return null

    }

    private fun getPath(node: AStarNode): List<AStarNode> {
        var currentNode = node
        val path: MutableList<AStarNode> = mutableListOf()
        path.add(currentNode)
        while (currentNode.parent != null) {
            path.add(0, currentNode.parent!!)
            currentNode = currentNode.parent!!
        }
        return path
    }

    private fun addAdjacentNodes(
        currentNode: AStarNode,
        openList: MutableList<AStarNode>,
        closedSet: Set<AStarNode>,
        finalNode: AStarNode
    ) {
        currentNode.node.neighbours.forEach { treeNode ->
            checkNode(currentNode, AStarNode(treeNode, finalNode), openList, closedSet)
        }
    }

    //всем нодам устанавливается сложность в 1, можно ее убрать по идее
    private fun checkNode(
        parentNode: AStarNode,
        node: AStarNode,
        openList: MutableList<AStarNode>,
        closedSet: Set<AStarNode>
    ) {
        if (!closedSet.contains(node)) {
            if (!openList.contains(node)) {
                node.setNodeData(parentNode, 1f)
                openList.add(node)
            } else {
                node.checkBetterPath(parentNode, 1f)
            }
        }
    }


    private fun getNextAStarNode(openList: List<AStarNode>): AStarNode {
        return openList.sortedBy { node -> node.f }[0]

    }






}