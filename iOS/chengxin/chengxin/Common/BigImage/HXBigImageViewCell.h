//
//  HXBigImageViewCell.h
//  FireShow
//
//  Created by wenxx on 2017/9/18.
//  Copyright © 2017年 VistaTeam. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HXBigImageViewCell : UICollectionViewCell
@property (nonatomic, strong) UIImageView           *iconImageView;

- (void)setImageWith:(UIImage *)image;
@end
