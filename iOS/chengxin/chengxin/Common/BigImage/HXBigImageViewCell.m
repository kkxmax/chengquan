//
//  HXBigImageViewCell.m
//  FireShow
//
//  Created by wenxx on 2017/9/18.
//  Copyright © 2017年 VistaTeam. All rights reserved.
//

#import "HXBigImageViewCell.h"
#import "Global.h"

@implementation HXBigImageViewCell

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self createSubView];
    }
    return self;
}

- (void)createSubView
{
    self.clipsToBounds = YES;
    self.iconImageView = [[UIImageView alloc] init];
    [self.contentView addSubview:self.iconImageView];
    self.iconImageView.frame = CGRectMake(0, 0, 200, 200);
    self.iconImageView.center = self.contentView.center;
//    self.iconImageView.clipsToBounds = YES;
//    [self.iconImageView mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.edges.equalTo(self);
//    }];
}

- (void)setImageWith:(UIImage *)image
{
    if (image == nil) {
        return;
    }
    CGSize size = [self operationImageWithImage:image];
//    .iconImageView.frame = CGRectMake((SCREEN_WIDTH - size.width)/2, (SCREEN_HEIGHT - size.height)/2, size.width, size.height);
    self.iconImageView.image = image;
    self.iconImageView.frame = CGRectMake(0, 0, size.width, size.height);
    self.iconImageView.center = self.contentView.center;
    self.iconImageView.contentMode = UIViewContentModeScaleAspectFill;
}

- (CGSize)operationImageWithImage:(UIImage *)image
{
    CGSize size = image.size;
    CGSize newSize;
    
    if (size.width >= SCREEN_WIDTH) {
        newSize.width = SCREEN_WIDTH;
        newSize.height = SCREEN_WIDTH * size.height /size.width;
        
        if (newSize.height > SCREEN_HEIGHT) {
            CGFloat maxHeigth = SCREEN_HEIGHT - 60;
            
            newSize.width = newSize.height / maxHeigth * SCREEN_WIDTH;
            newSize.height = maxHeigth;            
        }
    } else if (size.height >= SCREEN_HEIGHT) {
        newSize.height = 300;
        newSize.width = SCREEN_HEIGHT/300 * size.width;
    } else {
        newSize = size;
    }
    
    return newSize;
}

@end
