//
//  MSBrowseImageView.m
//  KnighteamFrame
//
//  Created by wenxx on 16/5/4.
//  Copyright © 2016年 wenxx. All rights reserved.
//

#import "MSBrowseImageView.h"
#import "UIImageView+WebCache.h"
#import "HXBigImageViewCell.h"
#import "Global.h"

@interface MSBrowseImageView ()<UICollectionViewDelegate,UICollectionViewDataSource>

{
    NSArray             *_imageArray;
    NSInteger           _currentIndx;
//    UIScrollView        *_myScrollView;
    UILabel             *_pageLabel;
    UICollectionView    *_bigCollectionView;
}

@property (nonatomic ,strong) UIImageView       *currentImageView;

@property (nonatomic ,strong) UIImageView       *frontImageView;

@property (nonatomic ,strong) UIImageView       *nextImageView;
@end

@implementation MSBrowseImageView
- (void)setCurrentIdx:(NSInteger)currentIdx
{
    _currentIdx = currentIdx;
    NSIndexPath *index = [NSIndexPath indexPathForRow:currentIdx inSection:0];
    [_bigCollectionView scrollToItemAtIndexPath:index atScrollPosition:UICollectionViewScrollPositionNone animated:NO];
    _pageLabel.text = [NSString stringWithFormat:@"%ld/%lu",(long)currentIdx + 1,(unsigned long)_imageArray.count];
}


- (void)setBigImageArray:(NSArray *)array withCurrentIndex:(NSInteger)index
{
    if (!array || array.count == 0) return;
    _imageArray = array;
    _currentIndx = index;
    [self createBigView];

    [_bigCollectionView setContentOffset:CGPointMake(SCREEN_WIDTH * index, 0)];
    _pageLabel.text = [NSString stringWithFormat:@"%ld/%lu",(long)index + 1,(unsigned long)_imageArray.count];
//    NSIndexPath *indexP = [NSIndexPath indexPathForRow:index inSection:0];
//    [_bigCollectionView scrollToItemAtIndexPath:indexP atScrollPosition:UICollectionViewScrollPositionNone animated:NO];
}

#pragma mark - 手势
- (void)tapView
{
    [self removeSuvView];
}

#pragma mark - 移除视图
- (void)removeSuvView
{
    [self.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [self removeFromSuperview];
}

- (void)createBigView
{
    if (_bigCollectionView) {
        _bigCollectionView.contentSize = CGSizeMake(SCREEN_WIDTH * _imageArray.count, 0);
        [_bigCollectionView reloadData];
        _pageLabel.text = [NSString stringWithFormat:@"%ld/%lu",(long)_currentIndx + 1,(unsigned long)_imageArray.count];
        return;
    }
    
//    self.backgroundColor = [UIColor blackColor];
//    UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapView)];
//    [self addGestureRecognizer:tapGesture];
    
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    layout.minimumLineSpacing = 0;
    layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
    layout.minimumInteritemSpacing = 0;
    UICollectionView *collectionVC = [[UICollectionView alloc] initWithFrame:CGRectZero collectionViewLayout:layout];
    collectionVC.delegate = self;
    collectionVC.dataSource = self;
    collectionVC.pagingEnabled = YES;
    _bigCollectionView = collectionVC;
    [collectionVC registerClass:[HXBigImageViewCell class] forCellWithReuseIdentifier:@"hx_big_cell"];
    [self addSubview:collectionVC];
    collectionVC.frame = self.bounds;
    _bigCollectionView.contentSize = CGSizeMake(SCREEN_WIDTH * _imageArray.count, 0);
    _pageLabel = [[UILabel alloc] init];
    _pageLabel.textColor = HEXCOLOR(0x999999);
    _pageLabel.font = [UIFont systemFontOfSize:18];
    [self addSubview:_pageLabel];
    _pageLabel.frame = CGRectMake(15, self.bounds.size.height - 20 - 40, self.bounds.size.width, 40);
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return _imageArray.count;
}


- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    HXBigImageViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"hx_big_cell" forIndexPath:indexPath];
    [cell.iconImageView sd_setImageWithURL:[NSURL URLWithString:_imageArray[indexPath.row]] placeholderImage:[UIImage imageNamed:@"no_image"] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
        [cell setImageWith:image];
    }];
    cell.iconImageView.contentMode = UIViewContentModeScaleAspectFit;
    
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return CGSizeMake(SCREEN_WIDTH, SCREEN_HEIGHT);
    NSString *imagerUrl = _imageArray[indexPath.row];
    UIImage *image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:imagerUrl]]];
    
    CGSize size = image.size;
    
    size.width = SCREEN_WIDTH;
    size.height = SCREEN_WIDTH * image.size.height /image.size.width;
    
    return size;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    [self removeSuvView];
    
    if (self.removeView) {
        self.removeView();
    }    
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
    CGFloat offSetX = scrollView.contentOffset.x;
    NSInteger index = offSetX / SCREEN_WIDTH ;
    _pageLabel.text = [NSString stringWithFormat:@"%ld/%lu",(long)index + 1,(unsigned long)_imageArray.count];
}

@end
