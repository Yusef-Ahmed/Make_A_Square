import java.util.concurrent.Semaphore;

class outer {
    static boolean check = false;
    static Semaphore sem = new Semaphore(1);

    static class Multithreading implements Runnable {
        boolean f = true; // no solution found
        int num_of_shape;
        int[][] grid;
        int[] choose;
        int[][][][] arr;
        int rotate;
        int id;

        public Multithreading(int num_of_shape, int rotate, int[][] grid, int[] choose, int[][][][] arr, int id) {
            this.num_of_shape = num_of_shape;
            this.grid = grid;
            this.choose = choose;
            this.arr = arr;
            this.rotate = rotate;
            this.id = id;
        }

        boolean valid(int x, int y, int i, int k) {
            for (int j = 0; j < 4; j++) {
                int pos_x = x + arr[i][k][j][0], pos_y = y + arr[i][k][j][1];
                if (pos_x < 0 || pos_x >= 4 || pos_y < 0 || pos_y >= 4 || grid[pos_x][pos_y] != -1) {
                    return false;
                }
            }
            return true;
        }

        void put(int i, int r) throws InterruptedException { // shape, rotate
            if (!f) {
                return;
            }
            int tetris = choose[i];
            int[][] loc = new int[4][2];

            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    if (valid(x, y, tetris, r)) {
                        for (int j = 0; j < 4; j++) {
                            int pos_x = x + arr[tetris][r][j][0], pos_y = y + arr[tetris][r][j][1];
                            grid[pos_x][pos_y] = i;
                            loc[j][0] = pos_x;
                            loc[j][1] = pos_y;
                        }
                        if (i == 3) {
                            f = false; // found solution
                            // acquiring the lock
                            sem.acquire();
                            check = true;
                            System.out.println("Thread number " + id);
                            for (int ii = 0; ii < 4; ii++) {
                                for (int kk = 0; kk < 4; kk++) {
                                    System.out.print(grid[ii][kk] + " ");
                                }
                                System.out.println();
                            }
                            System.out.println();
                            // Release the permit.
                            sem.release();
                            return;
                        } else {
                            for (int k = 0; (choose[i + 1] <= 1 && k < 4) || k < 2; k++) {
                                put(i + 1, k);
                                if (!f) return;
                            }
                        }
                        remove(loc);
                    }
                }
            }
        }

        void remove(int[][] loc) {
            for (int i = 0; i < 4; i++) {
                grid[loc[i][0]][loc[i][1]] = -1;
            }
        }

        public void run() {
            try {
//                System.out.println("Thread "+id+" started");
                put(num_of_shape, rotate);
//                System.out.println("Thread "+id+" ended");

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}