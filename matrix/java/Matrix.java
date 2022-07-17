package matrix.java;

import java.util.Arrays;
import java.util.Random;

public class Matrix<T> {

  private static final int DEFAULT_M = 3;
  private static final int DEFAULT_N = 3;

  private int m; // Row count
  private int n; // Column count

  private T[][] matrix;

  /**
   * Initialise m x n {@link Matrix} with default dimensions.
   */
  public Matrix() {
    this.m = DEFAULT_M;
    this.n = DEFAULT_N;
    this.matrix = build();
  }

  /**
   * Initialise m x n {@link Matrix} of specified {@code dimension}.
   *
   * @param dimension - dimension of {@link Matrix}
   */
  public Matrix(int dimension) {
    validateDimension(dimension);
    this.m = dimension;
    this.n = dimension;
    this.matrix = build();
  }

  /**
   * Initialise {@link Matrix} of dimension {@code m} x {@code n}.
   *
   * @param m m dimensions (rows)
   * @param n n dimensions (columns)
   */
  public Matrix(int m, int n) {
    validateDimension(m);
    validateDimension(n);
    this.m = m;
    this.n = n;
    this.matrix = build();
  }

  /**
   * Sets the specified {@code value} at coordinates {@code m}, {@code n} in this {@link Matrix}.
   *
   * @param value value to be associated at the specified coordinates
   * @param m     row coordinate
   * @param n     column coordinate
   * @return the updated {@link Matrix}
   */
  public T[][] set(T value, int m, int n) {
    matrix[m][n] = value;
    return matrix;
  }

  /**
   * Sets all {@link Matrix} coordinates to the specified {@code value}, overwriting existing
   * values.
   *
   * @param value value to be associated at all coordinates
   * @return the updated {@link Matrix}
   */
  public T[][] setAll(T value) {
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        matrix[i][j] = value;
      }
    }
    return matrix;
  }

  /**
   * Sets all {@code null} elements in this {@link Matrix} to the specified {@code value}, filling
   * the {@link Matrix}.
   *
   * @param value value to fill the {@link Matrix}
   * @return the updated {@link Matrix}
   */
  public T[][] fill(T value) {
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        if (matrix[i][j] == null) {
          matrix[i][j] = value;
        }
      }
    }
    return matrix;
  }

  /**
   * Sets all {@link Matrix} coordinates to a random value of the specified {@code clazz},
   * overwriting existing values.
   *
   * @param clazz class type to be associated at all coordinates
   * @return the updated {@link Matrix}
   */
  @SuppressWarnings("unchecked")
  public T[][] randomiseAll(Class<T> clazz) {
    if (clazz == Integer.class) {
      for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
          matrix[i][j] = (T) Integer.valueOf(new Random().nextInt(100));
        }
      }
      return matrix;
    } else if (clazz == String.class) {
      for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
          char[] symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
          char[] chars = new char[3];
          for (int k = 0; k < chars.length; k++) {
            chars[k] = symbols[new Random().nextInt(symbols.length)];
          }
          matrix[i][j] = (T) new String(chars);
        }
      }
      return matrix;
    } else if (clazz == Long.class) {
      for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
          matrix[i][j] = (T) Long.valueOf(new Random().nextLong(100));
        }
      }
      return matrix;
    } else if (clazz == Double.class) {
      for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
          matrix[i][j] = (T) Double.valueOf(new Random().nextDouble(100));
        }
      }
      return matrix;
    } else if (clazz == Float.class) {
      for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
          matrix[i][j] = (T) Float.valueOf(new Random().nextFloat(100));
        }
      }
      return matrix;
    } else if (clazz == Boolean.class) {
      for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
          matrix[i][j] = (T) Boolean.valueOf(new Random().nextDouble() > 0.5);
        }
      }
      return matrix;
    }
    throw new IllegalArgumentException("Unsupported class type " + clazz.getSimpleName());
  }

  /**
   * Clears the element at coordinates {@code m}, {@code n} in this {@link Matrix}.
   *
   * @param m row coordinate
   * @param n column coordinate
   * @return the updated {@link Matrix}
   */
  public T[][] clear(int m, int n) {
    matrix[m][n] = null;
    return matrix;
  }

  /**
   * Clears all elements in this {@link Matrix}.
   */
  public void clear() {
    matrix = build();
  }

  public enum Rotation {
    CLOCKWISE_90, CLOCKWISE_180, CLOCKWISE_270
  }

  /**
   * Rotates the elements in this {@link Matrix} by the specified {@link Rotation}.
   *
   * @param rotation degrees of rotation
   * @return the updated {@link Matrix}
   */
  public T[][] rotate(Rotation rotation) {
    T[][] matrixNew = null;
    switch (rotation) {
      case CLOCKWISE_90, CLOCKWISE_270 -> {
        // If not square matrix, build new with rotated dimensions
        matrixNew = isSquareMatrix() ? build(m, n) : build(n, m);
        for (int i = 0; i < m; i++) {
          for (int j = 0; j < n; j++) {
            if (rotation == Rotation.CLOCKWISE_90) {
              matrixNew[j][m - 1 - i] = matrix[i][j];
            } else {
              matrixNew[n - 1 - j][i] = matrix[i][j];
            }
          }
        }
        // If not square matrix, update global dimensions
        if (!isSquareMatrix()) {
          int mTemp = m;
          m = n;
          n = mTemp;
        }
      }
      case CLOCKWISE_180 -> {
        matrixNew = build();
        for (int i = 0; i < m; i++) {
          for (int j = 0; j < n; j++) {
            matrixNew[m - 1 - i][n - 1 - j] = matrix[i][j];
          }
        }
      }
    }

    matrix = matrixNew;
    return matrix;
  }

  public enum Plane {
    HORIZONTAL, VERTICAL
  }

  /**
   * Flips the elements in this {@link Matrix} along the specified {@link Plane}.
   *
   * @param plane plane to flip
   * @return the updated {@link Matrix}
   */
  public T[][] flip(Plane plane) {
    T[][] matrixNew = build();
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        switch (plane) {
          case HORIZONTAL -> matrixNew[m - 1 - i][j] = matrix[i][j];
          case VERTICAL -> matrixNew[i][n - 1 - j] = matrix[i][j];
        }
      }
    }
    matrix = matrixNew;
    return matrix;
  }

  /**
   * Prints this {@link Matrix}.
   *
   * <p>E.g.</p>
   * <p>[[1, 1, 1], [1, 1, 1], [1, 1, 1]]</p>
   */
  public void print() {
    System.out.println(Arrays.deepToString(matrix));
  }

  /**
   * Pretty prints this {@link Matrix}.
   *
   * <p>E.g.</p>
   * <p>[1, 1, 1]</p>
   * <p>[1, 1, 1]</p>
   * <p>[1, 1, 1]</p>
   */
  public void prettyPrint() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < m; i++) {
      sb.append("[");
      for (int j = 0; j < n; j++) {
        sb.append(matrix[i][j]);
        if (j == n - 1) {
          break;
        }
        sb.append(", ");
      }
      sb.append("]");
      if (i == m - 1) {
        break;
      }
      sb.append("\n");
    }
    System.out.println(sb);
  }

  private void validateDimension(int dimension) {
    if (dimension < 1) {
      throw new IllegalArgumentException(
          String.format("Matrix dimension %s must be greater than 0", dimension));
    }
  }

  private boolean isSquareMatrix() {
    return m == n;
  }

  private T[][] build() {
    return build(m, n);
  }

  @SuppressWarnings("unchecked")
  private T[][] build(int m, int n) {
    return (T[][]) new Object[m][n];
  }
}
