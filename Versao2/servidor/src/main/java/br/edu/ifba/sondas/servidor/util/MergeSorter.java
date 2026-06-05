package br.edu.ifba.sondas.servidor.util;

// O(n log n)
public class MergeSorter {
    public static void sort(String[] arr) {
        if (arr == null || arr.length < 2) return;
        String[] aux = new String[arr.length];
        mergeSort(arr, aux, 0, arr.length - 1);
    }
    private static void mergeSort(String[] a, String[] aux, int lo, int hi) {
        if (lo >= hi) return;
        int mid = (lo + hi)/2;
        mergeSort(a, aux, lo, mid);
        mergeSort(a, aux, mid+1, hi);
        merge(a, aux, lo, mid, hi);
    }
    private static void merge(String[] a, String[] aux, int lo, int mid, int hi) {
        int i = lo, j = mid+1, k = lo;
        while (i <= mid && j <= hi) {
            if (a[i].compareTo(a[j]) <= 0) aux[k++] = a[i++];
            else aux[k++] = a[j++];
        }
        while (i <= mid) aux[k++] = a[i++];
        while (j <= hi) aux[k++] = a[j++];
        for (k = lo; k <= hi; k++) a[k] = aux[k];
    }
}