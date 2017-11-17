//
//  EvaluateAwakenTableViewCell.h
//  chengxin
//
//  Created by seniorcoder on 11/4/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface EvaluateAwakenTableViewCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UIImageView *avatarImageView;
@property (nonatomic, strong) IBOutlet UILabel *nameLabel;
@property (nonatomic, strong) IBOutlet UILabel *commentLabel;
@property (nonatomic, strong) IBOutlet UILabel *evaluateLabel;
@property (nonatomic, strong) IBOutlet UILabel *timeLabel;
@property (nonatomic, strong) IBOutlet UILabel *agreeCountLabel;
@property (nonatomic, strong) IBOutlet UILabel *evaluateCountLabel;
@property (nonatomic, strong) IBOutlet UIScrollView *evaluateScrollView;

@end
