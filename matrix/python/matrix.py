from enum import Enum


class Matrix:
    DEFAULT_M = 3
    DEFAULT_N = 3

    def __init__(self, *args: int) -> None:
        """
        Initialise m x n Matrix.

        :param args:
          - 0 args: default dimensions used
          - 1 args: square matrix of dimension provided
          - 2 args: matrix of m x n dimensions provided
        """
        if not args:
            self.m = self.DEFAULT_M
            self.n = self.DEFAULT_N
            self.matrix = self.build()
        elif len(args) == 1:
            self.validate_dimension(args[0])
            self.m = args[0]
            self.n = args[0]
            self.matrix = self.build()
            pass
        elif len(args) == 2:
            self.validate_dimension(args[0])
            self.validate_dimension(args[1])
            self.m = args[0]
            self.n = args[1]
            self.matrix = self.build()
            pass
        else:
            raise ValueError('Pass in a maximum of 2 args')

    def set(self, value, m: int, n: int):
        """
        Sets the specified value at coordinates m, n in this Matrix.

        :param value: value to be associated at the specified coordinates
        :param m: row coordinate
        :param n: column coordinate
        :return: the updated Matrix
        """
        self.matrix[m][n] = value
        return self.matrix

    def set_all(self, value):
        """
        Sets all Matrix coordinates to the specified value, overwriting existing values.

        :param value: value to be associated at all coordinates
        :return: the updated Matrix
        """
        for i in range(self.m):
            for j in range(self.n):
                self.matrix[i][j] = value
        return self.matrix

    def fill(self, value):
        """
        Sets all None elements in this Matrix to the specified value, filling the Matrix.

        :param value: value to fill the Matrix
        :return: the updated Matrix
        """
        for i in range(self.m):
            for j in range(self.n):
                if self.matrix[i][j] is None:
                    self.matrix[i][j] = value
        return self.matrix

    def clear_element(self, m: int, n: int):
        """
        Clears the element at coordinates m, n in this Matrix.

        :param m: row coordinate
        :param n: column coordinate
        :return: the updated Matrix
        """
        self.matrix[m][n] = None
        return self.matrix

    def clear(self):
        """
        Clears all elements in this Matrix.
        """
        self.matrix = self.build()

    class Rotation(Enum):
        CLOCKWISE_90 = 1
        CLOCKWISE_180 = 2
        CLOCKWISE_270 = 3

    def rotate(self, rotation: Rotation):
        """
        Rotates the elements in this Matrix by the specified Rotation.

        :param rotation: degrees of rotation
        :return: the updated Matrix
        """
        if rotation in [Matrix.Rotation.CLOCKWISE_90, Matrix.Rotation.CLOCKWISE_270]:
            matrix_new = self.build_new(self.m, self.n) if self.is_square_matrix() else self.build_new(self.n, self.m)
            for i in range(self.m):
                for j in range(self.n):
                    if rotation == Matrix.Rotation.CLOCKWISE_90:
                        matrix_new[j][self.m - 1 - i] = self.matrix[i][j]
                    else:
                        matrix_new[self.n - 1 - j][i] = self.matrix[i][j]
            # If not square matrix, update global dimensions
            if not self.is_square_matrix():
                m_temp = self.m
                self.m = self.n
                self.n = m_temp
        elif rotation == Matrix.Rotation.CLOCKWISE_180:
            matrix_new = self.build()
            for i in range(self.m):
                for j in range(self.n):
                    matrix_new[self.m - 1 - i][self.n - 1 - j] = self.matrix[i][j]
        else:
            raise ValueError(f'Unrecognised Rotation: {rotation}')

        self.matrix = matrix_new
        return self.matrix

    class Plane(Enum):
        HORIZONTAL = 1
        VERTICAL = 2

    def flip(self, plane: Plane):
        """
        Flips the elements in this Matrix along the specified Plane.

        :param plane: plane to flip
        :return: the updated Matrix
        """
        matrix_new = self.build()
        for i in range(self.m):
            for j in range(self.n):
                if plane == Matrix.Plane.HORIZONTAL:
                    matrix_new[self.m - 1 - i][j] = self.matrix[i][j]
                elif plane == Matrix.Plane.VERTICAL:
                    matrix_new[i][self.n - 1 - j] = self.matrix[i][j]
                else:
                    raise ValueError(f'Unrecognised Plane: {plane}')
        self.matrix = matrix_new
        return self.matrix

    def __str__(self):
        """
        Overrides string representation of this Matrix.

        :return: string representation, e.g. [[1, 1, 1], [1, 1, 1], [1, 1, 1]]
        """
        return str(self.matrix)

    def pretty_print(self):
        """
        Pretty prints this Matrix

        E.g.
        [1, 1, 1]
        [1, 1, 1]
        [1, 1, 1]
        """
        s = ''
        for i in range(self.m):
            s += '['
            for j in range(self.n):
                if self.matrix[i][j]:
                    s += str(self.matrix[i][j])
                else:
                    s += 'None'
                if j == (self.n - 1):
                    break
                s += ', '
            s += ']'
            if i == (self.m - 1):
                break
            s += '\n'
        print(s)

    @staticmethod
    def validate_dimension(dimension: int):
        if dimension < 1:
            raise ValueError(f'Matrix dimension {dimension} must be greater than 0')

    def is_square_matrix(self):
        return self.m == self.n

    def build(self):
        return [[None] * self.n for _ in range(self.m)]

    @staticmethod
    def build_new(m: int, n: int):
        return [[None] * n for _ in range(m)]
