package com.nasscom.buildforindia.service;

import com.nasscom.buildforindia.model.BabyData;

public class MinHeap
{
    private BabyData[] Heap;
    private int size;
    private int maxsize;
 
    private static final int FRONT = 1;
 
    public MinHeap(int maxsize)
    {
        this.maxsize = maxsize;
        this.size = 0;
        Heap = new BabyData[this.maxsize + 1];
        Heap[0] = new BabyData();
        Heap[0].setScore(Double.MIN_VALUE);
    }
 
    private int parent(int pos)
    {
        return pos / 2;
    }
 
    private int leftChild(int pos)
    {
        return (2 * pos);
    }
 
    private int rightChild(int pos)
    {
        return (2 * pos) + 1;
    }
 
    private boolean isLeaf(int pos)
    {
        if (pos >=  (size / 2)  &&  pos <= size)
        { 
            return true;
        }
        return false;
    }
 
    private void swap(int fpos, int spos)
    {
        BabyData tmp;
        tmp = Heap[fpos];
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }
 
    private void minHeapify(int pos)
    {
        if (!isLeaf(pos))
        { 
            if ( Heap[pos].getScore() > Heap[leftChild(pos)].getScore()  || Heap[pos].getScore() > Heap[rightChild(pos)].getScore())
            {
                if (Heap[leftChild(pos)].getScore() < Heap[rightChild(pos)].getScore())
                {
                    swap(pos, leftChild(pos));
                    minHeapify(leftChild(pos));
                }else
                {
                    swap(pos, rightChild(pos));
                    minHeapify(rightChild(pos));
                }
            }
        }
    }
 
    public void insert(BabyData element)
    {
        Heap[++size] = element;
        int current = size;
 
        while (Heap[current].getScore() < Heap[parent(current)].getScore())
        {
            swap(current,parent(current));
            current = parent(current);
        }	
    }
 
    public void print()
    {
        for (int i = 1; i <= size / 2; i++ )
        {
            System.out.print(" PARENT : " + Heap[i] + " LEFT CHILD : " + Heap[2*i] 
                + " RIGHT CHILD :" + Heap[2 * i  + 1]);
            System.out.println();
        } 
    }
 
    public void minHeap()
    {
        for (int pos = (size / 2); pos >= 1 ; pos--)
        {
            minHeapify(pos);
        }
    }
 
    public BabyData remove()
    {
        BabyData popped = Heap[FRONT];
        Heap[FRONT] = Heap[size--]; 
        minHeapify(FRONT);
        return popped;
    }
 
    public static void main(String...arg)
    {
        System.out.println("The Min Heap is ");
        MinHeap minHeap = new MinHeap(15);
       
        minHeap.minHeap();
 
        minHeap.print();
        int front = minHeap.FRONT;
        System.out.println("Front: " + minHeap.Heap[minHeap.FRONT]);
        System.out.println("The Min val is " + minHeap.remove());
    }

	public BabyData[] getHeap() {
		return Heap;
	}

	public void setHeap(BabyData[] heap) {
		Heap = heap;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getMaxsize() {
		return maxsize;
	}

	public void setMaxsize(int maxsize) {
		this.maxsize = maxsize;
	}

	public static int getFront() {
		return FRONT;
	}
}